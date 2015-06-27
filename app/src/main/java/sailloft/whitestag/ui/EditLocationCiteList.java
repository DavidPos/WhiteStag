package sailloft.whitestag.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.db.ParkingHelper;
import sailloft.whitestag.model.CiteReasonData;
import sailloft.whitestag.model.LocationData;

public class EditLocationCiteList extends ListActivity {

    protected ParkingDataSource mParkingDataSource;
    private String type;
    private final String KEY_REASON = "Reason";
    private final String KEY_LOCATION = "Location";
    private FloatingActionButton addItem;
    private LocationData mLocationData;
    private SimpleAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_locatio_cite_list);
        mParkingDataSource = new ParkingDataSource(EditLocationCiteList.this);
        type = getIntent().getStringExtra("type");
        addItem = (FloatingActionButton)findViewById(R.id.addItemButton);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                final AlertDialog.Builder addAlert = new AlertDialog.Builder(EditLocationCiteList.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.loc_cite_dialog, null);
                addAlert.setTitle("New Addition");
                addAlert.setView(dialogView);
                final EditText mData = (EditText) dialogView.findViewById(R.id.addInfoText);
                mData.requestFocus();
                final TextView mAddInfo = (TextView) dialogView.findViewById(R.id.addInfoLabel);
                if (type.equals("location")) {
                    mAddInfo.setText("Location");

                } else {
                    mAddInfo.setText("Cite Reason");
                }


                addAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type.equals("location")) {

                            String info = mData.getText().toString();
                            mLocationData = new LocationData(info);
                            mParkingDataSource.insertLocation(mLocationData);
                            adapter.notifyDataSetChanged();
                        }
                        updateList();
                        if (type.equals("cite")) {
                            String cite = mData.getText().toString();
                            CiteReasonData mCiteReason = new CiteReasonData(cite);
                            mParkingDataSource.insertCiteReason(mCiteReason);
                            adapter.notifyDataSetChanged();

                        }
                        updateList();
                        adapter.notifyDataSetChanged();
                    }
                });

                addAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                addAlert.show();
                adapter.notifyDataSetChanged();


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
        updateList();



    }
    private void updateList(){
        if (type.equals("cite")){
            Cursor cursor = mParkingDataSource.selectAllCiteReasons();
            cursor.moveToFirst();
            ArrayList<HashMap<String, String>> allData =
                    new ArrayList<HashMap<String, String>>();
            while (!cursor.isAfterLast()) {
                int i = cursor.getColumnIndex(ParkingHelper.COLUMN_CITATIONS_TYPE);

                String reason = cursor.getString(i);



                HashMap<String, String> cite = new HashMap<String, String>();
                cite.put(KEY_REASON, reason);

                allData.add(cite);
                cursor.moveToNext();

            }

            String[] keys = {KEY_REASON};
            int[] ids = {android.R.id.text1, android.R.id.text2};
            adapter = new SimpleAdapter(this, allData,
                    android.R.layout.simple_list_item_1, keys, ids);
            setListAdapter(adapter);

        }
        if (type.equals("location")){
            Cursor cursor = mParkingDataSource.selectAllLocations();
            cursor.moveToFirst();
            ArrayList<HashMap<String, String>> allData =
                    new ArrayList<HashMap<String, String>>();
            while (!cursor.isAfterLast()) {
                int i = cursor.getColumnIndex(ParkingHelper.COLUMN_LOCATION);

                String currentLoc = cursor.getString(i);



                HashMap<String, String> location = new HashMap<>();
                location.put(KEY_LOCATION, currentLoc);

                allData.add(location);
                cursor.moveToNext();

            }

            String[] keys = {KEY_LOCATION};
            int[] ids = {android.R.id.text1, android.R.id.text2};
            adapter = new SimpleAdapter(this, allData,
                    android.R.layout.simple_list_item_1, keys, ids);
            setListAdapter(adapter);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_locatio_cite_list, menu);
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
