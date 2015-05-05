package sailloft.whitestag.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.edittext.FloatingLabelEditText;

import java.sql.SQLException;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.model.VehicleData;


public class VehicleAdd extends ActionBarActivity {
    FloatingLabelEditText state;
    FloatingLabelEditText plateNumber;

    FloatingLabelEditText vehicleModel;
    FloatingLabelEditText vehicleMake;
    FloatingLabelEditText vehicleYear;

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

        addVehicle = (FloatingActionButton)findViewById(R.id.addVehicleButton);

        mParkingDataSource = new ParkingDataSource(this);

        state = (FloatingLabelEditText)findViewById(R.id.statePickerVeh);

        Intent recent = getIntent();
        state.setInputWidgetText(recent.getStringExtra("State"));
        plateNumber.setInputWidgetText(recent.getStringExtra("Plate"));
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
                            state.getInputWidgetText().toString(),
                            vehicleYear.getInputWidgetText().toString(),
                            0);
                    mParkingDataSource.insertVehicle(mVehicle);
                    Intent intent = new Intent(VehicleAdd.this, VehicleInformation.class);
                    intent.putExtra(MainActivity.plateExtra, plateNumber.getInputWidgetText().toString());
                    intent.putExtra(MainActivity.stateExtra, state.getInputWidgetText().toString());
                    startActivity(intent);

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


}
