package sailloft.whitestag.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;


public class VehicleInformation extends ListActivity {

    protected ParkingDataSource mParkingDataSource;
    private ArrayList<String> mVehiclesPlate;
    private ArrayList<String> mVehicleMake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information);
        mParkingDataSource = new ParkingDataSource(VehicleInformation.this);
        final FloatingActionButton snapShot = (FloatingActionButton) findViewById(R.id.snapShotButton);
        snapShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleInformation.this, SnapShot.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton citeFAB = (FloatingActionButton) findViewById(R.id.citationButton);
        citeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleInformation.this, Citations.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mParkingDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mParkingDataSource.close();
    }
    protected void updateList(Cursor cursor){
        cursor.moveToFirst();
        //get vehicle id number from vehicle table
        //search citiations table for all citiations with the vehicle id
        //display in list
        //search snapshot table for all occurences of vehicle id
        //display in list
        while(!cursor.isAfterLast()){
            //do stuff


            cursor.moveToNext();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vehicle_information, menu);
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
