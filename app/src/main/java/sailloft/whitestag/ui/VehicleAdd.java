package sailloft.whitestag.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.edittext.FloatingLabelEditText;

import java.sql.SQLException;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.model.VehicleData;


public class VehicleAdd extends ActionBarActivity {
    private FloatingLabelEditText state;
    private FloatingLabelEditText plateNumber;


    private FloatingLabelEditText vehicleModel;
    private FloatingLabelEditText vehicleMake;
    private FloatingLabelEditText vehicleYear;
    private FloatingLabelEditText vehicleOwner;
    protected ParkingDataSource mParkingDataSource;
    private FloatingActionButton addVehicle;
    private VehicleData mVehicle;
    private int ownerId = 0;
    private String columns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_add);
        plateNumber = (FloatingLabelEditText)findViewById(R.id.plateInfo);
        vehicleModel =(FloatingLabelEditText)findViewById(R.id.vehicleModelSnap) ;
        vehicleMake =(FloatingLabelEditText)findViewById(R.id.vehicleMake) ;
        vehicleYear = (FloatingLabelEditText)findViewById(R.id.yearText);
        vehicleOwner =(FloatingLabelEditText)findViewById(R.id.vehicleOwner);

        state = (FloatingLabelEditText)findViewById(R.id.statePickerVeh);

        addVehicle = (FloatingActionButton)findViewById(R.id.addVehicleButton);

        if (savedInstanceState != null){
            plateNumber.setInputWidgetText(savedInstanceState.getString("plate"));
            vehicleMake.setInputWidgetText(savedInstanceState.getString("make"));
            vehicleModel.setInputWidgetText(savedInstanceState.getString("model"));
            state.setInputWidgetText(savedInstanceState.getString("state"));
            vehicleOwner.setInputWidgetText(savedInstanceState.getString("owner"));
            vehicleYear.setInputWidgetText(savedInstanceState.getString("year"));
        }else{
            //if you come from the vehicle information class this will complete the selected text views
            Intent recent = getIntent();
            state.setInputWidgetText(recent.getStringExtra("State"));
            plateNumber.setInputWidgetText(recent.getStringExtra("Plate"));
        }

        mParkingDataSource = new ParkingDataSource(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mParkingDataSource.open();

            addVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   String[] name = vehicleOwner.getInputWidgetText().toString().split(" ");
                    Log.e("STRING:" , name[0] + ":" + name[1]);
                    Cursor owner = mParkingDataSource.selectOwnerByName(name[0].replaceAll("\\s+",""),name[1].replaceAll("\\s+",""));
                 if(owner.getCount() <=0){
                        Intent intent = new Intent(VehicleAdd.this, OwnerAdd.class);
                        intent.putExtra("first", name[0]);
                        intent.putExtra("last", name[1]);
                        startActivityForResult(intent, 2);

                    }
                    else {
                        if (ownerId <= 0) {

                            owner.moveToFirst();
                            ownerId = owner.getInt(0);
                        }
                        mVehicle = new VehicleData(vehicleMake.getInputWidgetText().toString(),
                                vehicleModel.getInputWidgetText().toString(),
                                plateNumber.getInputWidgetText().toString(),
                                state.getInputWidgetText().toString(),
                                vehicleYear.getInputWidgetText().toString(),
                                ownerId);
                        mParkingDataSource.insertVehicle(mVehicle);
                        Intent intent = new Intent(VehicleAdd.this, VehicleInformation.class);
                        intent.putExtra(MainActivity.plateExtra, plateNumber.getInputWidgetText().toString());
                        intent.putExtra(MainActivity.stateExtra, state.getInputWidgetText().toString());
                        startActivity(intent);
                    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                ownerId = data.getIntExtra("ownerId",-1);
                Log.e("THIS IS THE OWNER:", Integer.toString(ownerId));
                if (ownerId == -1){
                    Toast.makeText(this, "There is a problem!!!!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("plate", plateNumber.getInputWidgetText().toString());
        outState.putString("make", vehicleMake.getInputWidgetText().toString());
        outState.putString("model", vehicleModel.getInputWidgetText().toString());
        outState.putString("state", state.getInputWidgetText().toString());
        outState.putString("owner", vehicleOwner.getInputWidgetText().toString());
        outState.putString("year", vehicleYear.getInputWidgetText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        plateNumber.setInputWidgetText(savedInstanceState.getString("plate"));
        vehicleMake.setInputWidgetText(savedInstanceState.getString("make"));
        vehicleModel.setInputWidgetText(savedInstanceState.getString("model"));
        state.setInputWidgetText(savedInstanceState.getString("state"));
        vehicleOwner.setInputWidgetText(savedInstanceState.getString("owner"));
        vehicleYear.setInputWidgetText(savedInstanceState.getString("year"));

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
