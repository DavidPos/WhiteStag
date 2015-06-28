package sailloft.whitestag.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.db.ParkingHelper;

public class VehicleList extends ListActivity {
    private final String KEY_PLATE = "Plate";
    private final String KEY_STATE = "State";
    protected ParkingDataSource mParkingDataSource;
    private ArrayList<HashMap<String, String>> allVehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicle_list);
        allVehicles = new ArrayList<>();
        mParkingDataSource = new ParkingDataSource(VehicleList.this);

    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            mParkingDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        String state = intent.getStringExtra(MainActivity.stateExtra);
        if (state == null) {
            Cursor cursor = mParkingDataSource.selectAllVehicles();
            updateList(cursor);



        } else {
            Cursor cursor = mParkingDataSource.selectAllVehiclesByState(intent.getStringExtra(MainActivity.stateExtra));
            updateList(cursor);
        }
    }
        @Override
        protected void onPause () {
            super.onPause();
            mParkingDataSource.close();
        }

        @Override
        public void onListItemClick (ListView l, View v,int position, long id){
            super.onListItemClick(l, v, position, id);
            HashMap vehicle = allVehicles.get(position);
            String plate = (String) vehicle.get(KEY_PLATE);
            String state = (String) vehicle.get(KEY_STATE);


            Intent intent = new Intent(VehicleList.this, VehicleInformation.class);
            intent.putExtra(MainActivity.plateExtra, plate);
            intent.putExtra(MainActivity.stateExtra, state);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            allVehicles.clear();
            startActivity(intent);
        }


    protected void updateList(Cursor vehicles){
        vehicles.moveToFirst();
        while (!vehicles.isAfterLast()) {

            int i = vehicles.getColumnIndex(ParkingHelper.COLUMN_PLATE_NUMBER);
            int z = vehicles.getColumnIndex(ParkingHelper.COLUMN_PLATE_STATE);
            String plate = vehicles.getString(i);
            String state = vehicles.getString(z);


            HashMap<String, String> vehicle = new HashMap<String, String>();
            vehicle.put(KEY_PLATE, plate);
            vehicle.put(KEY_STATE, state);
            allVehicles.add(vehicle);
            vehicles.moveToNext();
        }
        String[] keys = {KEY_PLATE, KEY_STATE};
        int[] ids = {android.R.id.text1, android.R.id.text2};
        SimpleAdapter adapter = new SimpleAdapter(this, allVehicles,
                android.R.layout.simple_list_item_2, keys, ids);
        setListAdapter(adapter);




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vehicle_list, menu);
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
