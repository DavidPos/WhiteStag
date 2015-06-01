package sailloft.whitestag.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker;
import com.marvinlabs.widget.floatinglabel.itempicker.ItemPickerListener;
import com.marvinlabs.widget.floatinglabel.itempicker.StringPickerDialogFragment;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

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
    private String mPlate;
    private String mState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_shot);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        addSnap = (FloatingActionButton)findViewById(R.id.addSnapButton);
        mParkingDataSource = new ParkingDataSource(this);

        Intent intent = getIntent();
        vehicleId = intent.getIntExtra("vehicleID", -1);
        ownerId = intent.getIntExtra("ownerID", -1);
        mPlate = intent.getStringExtra("Plate");
        mState = intent.getStringExtra("State");

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
                SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm", Locale.US);



                mSnapShotData = new SnapShotData(ownerId,
                        sdf.format(new Date()),
                               vehicleId,
                               locationPicker.getInputWidget().getText().toString());
                mParkingDataSource.insertSnapShot(mSnapShotData);
                Toast.makeText(SnapShot.this, "Vehicle added to SnapShot", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(SnapShot.this, VehicleInformation.class);
                intent.putExtra(MainActivity.stateExtra,mState);
                intent.putExtra(MainActivity.plateExtra, mPlate);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SnapShot.this, VehicleInformation.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(MainActivity.plateExtra, mPlate);
            intent.putExtra(MainActivity.stateExtra, mState);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
