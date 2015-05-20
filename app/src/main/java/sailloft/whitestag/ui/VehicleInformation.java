package sailloft.whitestag.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.db.ParkingHelper;


public class VehicleInformation extends ListActivity {

    protected ParkingDataSource mParkingDataSource;

    private String mState;
    private String mPlate;
    private int vehicleID;
    private int ownerId;
    private final String KEY_DATE = "date";
    private final String KEY_TYPE= "citeType";
    private static final String TAG = VehicleInformation.class.getSimpleName();
    private TextView vehicleLabel;
    private TextView ownerLabel;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information);
        Intent intent = getIntent();

        mPlate = intent.getStringExtra(MainActivity.plateExtra);
        mState = intent.getStringExtra(MainActivity.stateExtra);

        mParkingDataSource = new ParkingDataSource(VehicleInformation.this);
        vehicleLabel = (TextView)findViewById(R.id.vehicleLabel);
        ownerLabel = (TextView)findViewById(R.id.ownerLabelVI);


        final FloatingActionButton snapShot = (FloatingActionButton) findViewById(R.id.snapShotButton);
        snapShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleInformation.this, SnapShot.class);
                intent.putExtra("vehicleID", vehicleID);
                intent.putExtra("ownerID", ownerId);
                startActivity(intent);
            }
        });

        final FloatingActionButton citeFAB = (FloatingActionButton) findViewById(R.id.citationButton);
        citeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleInformation.this, Citations.class);
                intent.putExtra("vehicleID", vehicleID);
                intent.putExtra("ownerID", ownerId);
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
       Cursor vehicle = mParkingDataSource.selectOneVehicleByPlate(mPlate, mState);

        if (vehicle.getCount() <=0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Unable to locate! Do you want to add this vehicle?")
                    .setTitle("Vehicle not found!")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(VehicleInformation.this,VehicleAdd.class);
                            intent.putExtra("Plate", mPlate);
                            intent.putExtra("State", mState);
                            setResult(RESULT_OK, intent);
                            startActivity(intent);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(VehicleInformation.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else {
            vehicle.moveToFirst();

            int i = vehicle.getColumnIndex(ParkingHelper.COLUMN_OWNER);
            int o = vehicle.getColumnIndex(ParkingHelper.COLUMN_ID);
            int z = vehicle.getColumnIndex(ParkingHelper.COLUMN_PLATE_NUMBER);

            vehicleLabel.setText("Vehicle: " + vehicle.getString(z));

            vehicleID = vehicle.getInt(o);
            ownerId = vehicle.getInt(i);

            Cursor citations = mParkingDataSource.selectCitationsForVehicle(vehicleID);
            Cursor owner = mParkingDataSource.selectVehicleOwner(ownerId);
            owner.moveToFirst();
            int q = owner.getColumnIndex(ParkingHelper.COLUMN_FIRST_NAME);
            int w = owner.getColumnIndex(ParkingHelper.COLUMN_LAST_NAME);
            ownerLabel.setText("Owner:  " + owner.getString(q) + " " + owner.getString(w));

            updateList(citations);
            ownerLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        mParkingDataSource.close();
    }
    protected void updateList(Cursor cursor){
        cursor.moveToFirst();
        if (cursor.getCount() <=0){
            Toast.makeText(this,"Vehicle has no citations", Toast.LENGTH_LONG).show();

        }else {
            Log.e("START", "START Of LIST");
            //get vehicle id number from vehicle table
            //search citiations table for all citiations with the vehicle id
            //display in list
            //search snapshot table for all occurences of vehicle id
            //display in list
            ArrayList<HashMap<String, String>> allCites =
                    new ArrayList<HashMap<String, String>>();
            while (!cursor.isAfterLast()) {
                //do stuff
                int i = cursor.getColumnIndex(ParkingHelper.COLUMN_DATE_TIME);
                int z = cursor.getColumnIndex(ParkingHelper.COLUMN_CITATIONS_TYPE);
                Integer date = cursor.getInt(i);
                java.util.Date time = new java.util.Date((long) date * 1000);
                String conDate = time.toString();
                String cite = cursor.getString(z);
                Log.e("CITATION", conDate + cite);

                HashMap<String, String> cites = new HashMap<String, String>();
                cites.put(KEY_DATE, conDate);
                cites.put(KEY_TYPE, cite);
                allCites.add(cites);
                cursor.moveToNext();
            }
            String[] keys = {KEY_TYPE, KEY_DATE};
            int[] ids = {android.R.id.text1, android.R.id.text2};
            SimpleAdapter adapter = new SimpleAdapter(this, allCites,
                    android.R.layout.simple_list_item_2, keys, ids);
            setListAdapter(adapter);


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
