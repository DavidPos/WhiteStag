package sailloft.whitestag.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import sailloft.whitestag.model.VehicleData;


public class VehicleAdd extends ActionBarActivity implements ItemPickerListener<String>, FloatingLabelItemPicker.OnItemPickerEventListener<String> {
    FloatingLabelItemPicker<String> statePicker;
    FloatingLabelEditText plateNumber;

    FloatingLabelEditText vehicleModel;
    FloatingLabelEditText vehicleMake;
    FloatingLabelEditText vehicleYear;
    FloatingLabelEditText vehicleOwner;
    protected ParkingDataSource mParkingDataSource;
    FloatingActionButton addVehicle;
    private VehicleData mVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_add);
        plateNumber = (FloatingLabelEditText)findViewById(R.id.plateInfo);
        vehicleModel =(FloatingLabelEditText)findViewById(R.id.vehicleModel) ;
        vehicleMake =(FloatingLabelEditText)findViewById(R.id.vehicleMake) ;
        vehicleYear = (FloatingLabelEditText)findViewById(R.id.yearText);
        vehicleOwner = (FloatingLabelEditText)findViewById(R.id.vehicleOwner);
        addVehicle = (FloatingActionButton)findViewById(R.id.addVehicleButton);

        mParkingDataSource = new ParkingDataSource(this);

        statePicker = (FloatingLabelItemPicker<String>)findViewById(R.id.statePickerVeh);

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
    protected void onResume() {
        super.onResume();
        try {
            mParkingDataSource.open();
            addVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVehicle = new VehicleData(vehicleMake.getInputWidgetText().toString(),
                            vehicleModel.getInputWidgetText().toString(),
                            plateNumber.getInputWidgetText().toString(),
                            statePicker.getSelectedItems().toString(),
                            vehicleYear.getInputWidgetText().toString(),
                            1);
                    mParkingDataSource.insertVehicle(mVehicle);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mParkingDataSource.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vehicle_add, menu);
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
    public void onCancelled(int i) {

    }

    @Override
    public void onItemsSelected(int i, int[] selectedIndices) {
        statePicker.setSelectedIndices(selectedIndices);

    }

    @Override
    public void onSelectionChanged(FloatingLabelItemPicker<String> stringFloatingLabelItemPicker, Collection<String> strings) {

    }
}
