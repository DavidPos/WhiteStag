package sailloft.whitestag.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker;
import com.marvinlabs.widget.floatinglabel.itempicker.ItemPickerListener;
import com.marvinlabs.widget.floatinglabel.itempicker.StringPickerDialogFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.model.SnapShotData;


public class SnapShot extends ActionBarActivity implements ItemPickerListener<String> {
    FloatingLabelItemPicker<String> locationPicker;
    FloatingActionButton addSnap;
    protected ParkingDataSource mParkingDataSource;
    private int vehicleId;
    private int ownerId;
    private SnapShotData mSnapShotData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_shot);
        addSnap = (FloatingActionButton)findViewById(R.id.addSnapButton);
        mParkingDataSource = new ParkingDataSource(this);

        Intent intent = getIntent();
        vehicleId = intent.getIntExtra("vehicleID", -1);
        ownerId = intent.getIntExtra("ownerID", -1);

        locationPicker = (FloatingLabelItemPicker<String>)findViewById(R.id.locationPicker);
        String[] location = getResources().getStringArray(R.array.locations);

        locationPicker.setAvailableItems(new ArrayList<>(Arrays.asList(location)));
        locationPicker.setWidgetListener(new FloatingLabelItemPicker.OnWidgetEventListener<String>() {
            @Override
            public void onShowItemPickerDialog(FloatingLabelItemPicker<String> source) {
                StringPickerDialogFragment location = StringPickerDialogFragment.newInstance(
                        source.getId(),
                        "Select Location",
                        "OK", "Cancel",
                        false,
                        source.getSelectedIndices(),
                        new ArrayList<>(source.getAvailableItems()));

                location.show(getSupportFragmentManager(), "ItemPicker");
            }
        });
    }

    @Override
    public void onCancelled(int pickerId) {
    }

    @Override
    public void onItemsSelected(int pickerId, int[] selectedIndices) {

            locationPicker.setSelectedIndices(selectedIndices);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mParkingDataSource.open();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        addSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Date date = new java.util.Date();
                mSnapShotData = new SnapShotData(ownerId,
                        date.getTime(),
                        vehicleId,
                        locationPicker.getInputWidget().getText().toString());
                mParkingDataSource.insertSnapShot(mSnapShotData);
                Toast.makeText(SnapShot.this, "Vehicle added to SnapShot", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SnapShot.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        mParkingDataSource.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_snap_shot, menu);
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
}
