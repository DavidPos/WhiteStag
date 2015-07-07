package sailloft.whitestag.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.edittext.FloatingLabelEditText;

import java.sql.SQLException;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.db.ParkingHelper;
import sailloft.whitestag.model.OwnerData;
import sailloft.whitestag.model.VehicleData;

public class VehicleEdit extends ActionBarActivity {

    private FloatingLabelEditText state;
    private FloatingLabelEditText plateNumber;


    private FloatingLabelEditText vehicleModel;
    private FloatingLabelEditText vehicleMake;
    private FloatingLabelEditText vehicleYear;
    private FloatingLabelEditText vehicleOwner;
    protected ParkingDataSource mParkingDataSource;
    private FloatingActionButton addVehicle;
    private VehicleData mVehicle;
    private int mOwner;
    private OwnerData tempOwner;


    private String mPlate;
    private String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        mOwner = intent.getIntExtra("ownerId", -1);

        mPlate = intent.getStringExtra(MainActivity.plateExtra);
        mState = intent.getStringExtra(MainActivity.stateExtra);



        plateNumber = (FloatingLabelEditText)findViewById(R.id.plateInfo);
        vehicleModel =(FloatingLabelEditText)findViewById(R.id.vehicleModelSnap) ;
        vehicleMake =(FloatingLabelEditText)findViewById(R.id.vehicleMake) ;
        vehicleYear = (FloatingLabelEditText)findViewById(R.id.yearText);
        vehicleOwner =(FloatingLabelEditText)findViewById(R.id.vehicleOwner);

        state = (FloatingLabelEditText)findViewById(R.id.statePickerVeh);

        addVehicle = (FloatingActionButton)findViewById(R.id.addVehicleButton);

        mParkingDataSource = new ParkingDataSource(this);


    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            mParkingDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor vehicle = mParkingDataSource.selectOneVehicleByPlate(mPlate, mState);
        Cursor owner = mParkingDataSource.selectVehicleOwner(mOwner);
        owner.moveToFirst();
        vehicle.moveToFirst();
        int q = vehicle.getColumnIndex(ParkingHelper.COLUMN_MAKE);
        int w = vehicle.getColumnIndex(ParkingHelper.COLUMN_MODEL);
        int z = vehicle.getColumnIndex(ParkingHelper.COLUMN_YEAR);

        int a = owner.getColumnIndex(ParkingHelper.COLUMN_FIRST_NAME);
        int b = owner.getColumnIndex(ParkingHelper.COLUMN_LAST_NAME);



        vehicleMake.setInputWidgetText(vehicle.getString(q));
        vehicleModel.setInputWidgetText(vehicle.getString(w));
        vehicleYear.setInputWidgetText(vehicle.getString(z));
        vehicleOwner.setInputWidgetText(owner.getString(a) + " " + owner.getString(b));
        state.setInputWidgetText(mState);
        plateNumber.setInputWidgetText(mPlate);


        addVehicle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (vehicleOwner.getInputWidgetText().toString().equals("")){
                    //update vehicle if the owner is unknown we create a blank owner into the database and link it to the vehicle
                    tempOwner = new OwnerData(null, null, null, null, null);

                    mVehicle = new VehicleData(vehicleMake.getInputWidgetText().toString(),
                            vehicleModel.getInputWidgetText().toString(),
                            plateNumber.getInputWidgetText().toString(),
                            state.getInputWidgetText().toString(),
                            vehicleYear.getInputWidgetText().toString(),
                            (int)mParkingDataSource.insertOwnerReturnId(tempOwner));
                    mParkingDataSource.updateVehicle(mVehicle, mPlate, mState);
                    Intent intent = new Intent(VehicleEdit.this, VehicleInformation.class);
                    intent.putExtra(MainActivity.plateExtra, plateNumber.getInputWidgetText().toString());
                    intent.putExtra(MainActivity.stateExtra, state.getInputWidgetText().toString());
                    startActivity(intent);



                }
                else {
                    String[] name = vehicleOwner.getInputWidgetText().toString().split(" ");


                    Cursor owner = mParkingDataSource.selectOwnerByName(name[0].replaceAll("\\s+", ""), name[1].replaceAll("\\s+", ""));
                    if (owner.getCount() <= 0) {
                        //no owner found so sent to owner edit activity
                        Intent intent = new Intent(VehicleEdit.this, OwnerEdit.class);

                        intent.putExtra("first", name[0]);
                        intent.putExtra("last", name[1]);
                        intent.putExtra("ownerId", mOwner);
                        intent.putExtra(MainActivity.plateExtra, mPlate);
                        intent.putExtra(MainActivity.stateExtra, mState);

                        startActivityForResult(intent, 2);

                    } else {
                            //owner found so update vehicle

                        mVehicle = new VehicleData(vehicleMake.getInputWidgetText().toString(),
                                vehicleModel.getInputWidgetText().toString(),
                                plateNumber.getInputWidgetText().toString(),
                                state.getInputWidgetText().toString(),
                                vehicleYear.getInputWidgetText().toString(), mOwner);
                        mParkingDataSource.updateVehicle(mVehicle, mPlate, mState);
                        mState = state.getInputWidgetText().toString();
                        mPlate =  plateNumber.getInputWidgetText().toString();


                        Intent intent = new Intent(VehicleEdit.this, VehicleInformation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra(MainActivity.plateExtra, mPlate);
                        intent.putExtra(MainActivity.stateExtra, mState);
                        startActivity(intent);
                    }
                }
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
            Intent intent = new Intent(VehicleEdit.this, VehicleInformation.class);
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
        getMenuInflater().inflate(R.menu.menu_owner_edit, menu);
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