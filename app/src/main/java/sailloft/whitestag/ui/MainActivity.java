package sailloft.whitestag.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.edittext.FloatingLabelEditText;
import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker;
import com.marvinlabs.widget.floatinglabel.itempicker.ItemPickerListener;
import com.marvinlabs.widget.floatinglabel.itempicker.StringPickerDialogFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;


public class MainActivity extends ActionBarActivity implements ItemPickerListener<String>, FloatingLabelItemPicker.OnItemPickerEventListener<String> {
    FloatingLabelItemPicker<String> statePicker;

    private FloatingActionButton plateSearch;
    protected ParkingDataSource mDataSource;
    private FloatingLabelEditText plate;

    private String mPlate;
    private String mState;
    public static final String plateExtra = "PLATE_NUMBER";
    public static final String stateExtra = "PLATE_STATE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataSource = new ParkingDataSource(MainActivity.this);
        plate = (FloatingLabelEditText)findViewById(R.id.plateNumberEdit);


        plateSearch = (FloatingActionButton)findViewById(R.id.searchFab);

        plateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlate = plate.getInputWidgetText().toString();
                mPlate = mPlate.toUpperCase();
                mState = statePicker.getSelectedItems().toString();
                mState = mState.replaceAll("\\W", "");
                mState = mState.replaceAll("\\s", "");

                if (mState.isEmpty() && mPlate.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, VehicleList.class);
                    plate.setInputWidgetText("");
                    statePicker.setSelectedIndices(null);
                    startActivity(intent);
                } else if(!mState.isEmpty() && !mPlate.isEmpty()) {


                    Intent intent = new Intent(MainActivity.this, VehicleInformation.class);
                    intent.putExtra(plateExtra, mPlate);
                    intent.putExtra(stateExtra, mState);
                    plate.setInputWidgetText("");
                    statePicker.setSelectedIndices(null);
                    startActivity(intent);


                }else if (!mState.isEmpty() && mPlate.isEmpty()){
                    Intent intent = new Intent(MainActivity.this, VehicleList.class);
                    intent.putExtra(stateExtra, mState);
                    plate.setInputWidgetText("");
                    statePicker.setSelectedIndices(null);
                    startActivity(intent);
                }
            }
        });
        statePicker = (FloatingLabelItemPicker<String>)findViewById(R.id.statePicker);
        String[] states = getResources().getStringArray(R.array.states);

        statePicker.setAvailableItems(new ArrayList<String>(Arrays.asList(states)));
        statePicker.setWidgetListener(new FloatingLabelItemPicker.OnWidgetEventListener<String>() {
            @Override
            public void onShowItemPickerDialog(FloatingLabelItemPicker<String> source) {
                StringPickerDialogFragment statePicker = StringPickerDialogFragment.newInstance(
                        source.getId(),
                        "Select State",
                        "OK", "Cancel",
                        false,
                        source.getSelectedIndices(),
                        new ArrayList<>(source.getAvailableItems()));

                statePicker.show(getSupportFragmentManager(), "ItemPicker");
            }
        });
    }

    @Override
    public void onCancelled(int pickerId) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    public void onItemsSelected(int pickerId, int[] selectedIndices) {
        statePicker.setSelectedIndices(selectedIndices);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectionChanged(FloatingLabelItemPicker<String> stringFloatingLabelItemPicker, Collection<String> strings) {

    }
}
