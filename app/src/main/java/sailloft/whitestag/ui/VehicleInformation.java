package sailloft.whitestag.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.db.ParkingHelper;
import sailloft.whitestag.ui.vehicleInfoList.Header;
import sailloft.whitestag.ui.vehicleInfoList.Item;
import sailloft.whitestag.ui.vehicleInfoList.ListHeadersAdapter;
import sailloft.whitestag.ui.vehicleInfoList.ListItem;


public class VehicleInformation extends ListActivity {

    protected ParkingDataSource mParkingDataSource;

    private String mState;
    private String mPlate;
    private int vehicleID;
    private int ownerId;
    private HashMap<String, String> cites;

    private static final String TAG = VehicleInformation.class.getSimpleName();
    private TextView vehicleLabel;
    private TextView ownerLabel;

    private List<Item> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_information);
        if (savedInstanceState != null){
            mState = savedInstanceState.getString("state");
            mPlate = savedInstanceState.getString("plate");
        }
        Intent intent = getIntent();
         items = new ArrayList<Item>();


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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("ownerID", ownerId);
                intent.putExtra("Plate", mPlate);
                intent.putExtra("State", mState);
                startActivity(intent);
            }
        });

        final FloatingActionButton citeFAB = (FloatingActionButton) findViewById(R.id.citationButton);
        citeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleInformation.this, Citations.class);
                intent.putExtra("vehicleID", vehicleID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("ownerID", ownerId);
                intent.putExtra("Plate", mPlate);
                intent.putExtra("State", mState);

                startActivity(intent);
            }
        });

        ownerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleInformation.this, OwnerEdit.class);
                intent.putExtra("ownerId", ownerId);
                intent.putExtra(MainActivity.plateExtra, mPlate);
                intent.putExtra(MainActivity.stateExtra, mState);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        vehicleLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleInformation.this, VehicleEdit.class);
                intent.putExtra("ownerId", ownerId);
                intent.putExtra(MainActivity.plateExtra, mPlate);
                intent.putExtra(MainActivity.stateExtra, mState);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        Intent intent = getIntent();
        mPlate = intent.getStringExtra(MainActivity.plateExtra);
        mState = intent.getStringExtra(MainActivity.stateExtra);


            Cursor vehicle = mParkingDataSource.selectOneVehicleByPlate(mPlate, mState);

            if (vehicle.getCount() <= 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Unable to locate! Do you want to add this vehicle?")
                        .setTitle("Vehicle not found!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(VehicleInformation.this, VehicleAdd.class);
                                intent.putExtra("Plate", mPlate);
                                intent.putExtra("State", mState);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

            } else {
                vehicle.moveToFirst();

                int i = vehicle.getColumnIndex(ParkingHelper.COLUMN_OWNER);
                int o = vehicle.getColumnIndex(ParkingHelper.COLUMN_ID);
                int z = vehicle.getColumnIndex(ParkingHelper.COLUMN_PLATE_NUMBER);

                vehicleLabel.setText("Vehicle: " + vehicle.getString(z));

                vehicleID = vehicle.getInt(o);
                ownerId = vehicle.getInt(i);

                Cursor citations = mParkingDataSource.selectCitationsForVehicle(vehicleID);
                Cursor owner = mParkingDataSource.selectVehicleOwner(ownerId);
                Cursor snap = mParkingDataSource.snapShotByVehicleId(vehicleID);
                if (snap != null && citations != null) {
                    owner.moveToFirst();
                    int q = owner.getColumnIndex(ParkingHelper.COLUMN_FIRST_NAME);
                    int w = owner.getColumnIndex(ParkingHelper.COLUMN_LAST_NAME);
                    if(owner.getString(q)== null && owner.getString(w) == null){
                        ownerLabel.setText("Owner is unknown");
                    }else {
                        ownerLabel.setText("Owner:  " + owner.getString(q) + " " + owner.getString(w));
                    }
                    updateList(citations, snap);
                } else {
                    if (snap != null) {
                        items.add(new Header("SNAPSHOT"));
                        snap.moveToFirst();

                        while (!snap.isAfterLast()) {
                            int intDate = snap.getColumnIndex(ParkingHelper.COLUMN_DATE_TIME);
                            int intLoc = snap.getColumnIndex(ParkingHelper.COLUMN_LOCATION);
                            String date = snap.getString(intDate);
                            String location = snap.getString(intLoc);

                            items.add(new ListItem(location, date));

                            snap.moveToNext();
                        }
                        ListHeadersAdapter adapter = new ListHeadersAdapter(this, items);
                        setListAdapter(adapter);

                    }
                    if (citations != null) {
                        citations.moveToFirst();
                        items.add(new Header("CITATIONS"));
                        while (!citations.isAfterLast()) {

                            int citDate = citations.getColumnIndex(ParkingHelper.COLUMN_DATE_TIME);
                            int citType = citations.getColumnIndex(ParkingHelper.COLUMN_CITATIONS_TYPE);
                            String date = citations.getString(citDate);
                            String cite = citations.getString(citType);

                            items.add(new ListItem(date, cite));


                            citations.moveToNext();

                        }
                        ListHeadersAdapter adapter = new ListHeadersAdapter(this, items);
                        setListAdapter(adapter);
                    }


                    if (citations == null && snap == null) {
                        Log.i(TAG, "No citations");
                    }

                }
            }



    }

    @Override
    protected void onPause(){
        super.onPause();
        mParkingDataSource.close();
        items.clear();
    }
    protected void updateList(Cursor cites, Cursor snaps){
        snaps.moveToFirst();
        cites.moveToFirst();


            //get vehicle id number from vehicle table
            //search citiations table for all citiations with the vehicle id
            //display in list
            //search snapshot table for all occurences of vehicle id
            //display in list

            items.add(new Header("CITATIONS"));
            while (!cites.isAfterLast()) {
                //do stuff
                int i = cites.getColumnIndex(ParkingHelper.COLUMN_DATE_TIME);
                int z = cites.getColumnIndex(ParkingHelper.COLUMN_CITATIONS_TYPE);
                String date = cites.getString(i);

                String cite = cites.getString(z);
                items.add(new ListItem(cite, date));

                cites.moveToNext();
            }
            items.add(new Header("SNAPSHOT"));
            while (!snaps.isAfterLast()){
                int i = snaps.getColumnIndex(ParkingHelper.COLUMN_DATE_TIME);
                int z = snaps.getColumnIndex(ParkingHelper.COLUMN_LOCATION);
                String date = snaps.getString(i);
                String location = snaps.getString(z);

                items.add(new ListItem(location, date));
                snaps.moveToNext();
            }

            ListHeadersAdapter adapter = new ListHeadersAdapter(this, items);
            setListAdapter(adapter);


        }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(VehicleInformation.this, "CLick!", Toast.LENGTH_LONG).show();
        Item item = items.get(position);
        Class mClass = item.getClass();

       //if(mClass.isInstance(ListHeadersAdapter.RowType.LIST_ITEM.ordinal())){

           ListItem lItem = (ListItem) item;
           String date = lItem.getStr2();
            Log.i("Date", date);
           Intent intent = new Intent(VehicleInformation.this, CitationEdit.class);
           intent.putExtra("vehicleID", vehicleID);
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           intent.putExtra("Date", date);
           intent.putExtra("Plate", mPlate);
           intent.putExtra("State", mState);

           startActivity(intent);
       //}




    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(VehicleInformation.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(MainActivity.plateExtra, mPlate);
            intent.putExtra(MainActivity.stateExtra, mState);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("state", mState);
        outState.putString("plate", mPlate);
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
