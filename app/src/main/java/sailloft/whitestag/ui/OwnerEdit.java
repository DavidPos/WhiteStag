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

public class OwnerEdit extends ActionBarActivity {
    protected ParkingDataSource mParkingDataSource;
    private FloatingLabelEditText mFirstName;
    private FloatingLabelEditText mLastName;
    private FloatingLabelEditText mPermits;
    private FloatingLabelEditText mBuilding;
    private FloatingLabelEditText mDepartment;
    private FloatingActionButton mAddOwner;
    private OwnerData mOwner;
    private int ownerID;
    private int ownerCheck;
    private String mPlate;
    private String mState;
    private Boolean checkNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        ownerID = intent.getIntExtra("ownerId", -1);
        mPlate = intent.getStringExtra(MainActivity.plateExtra);
        mState = intent.getStringExtra(MainActivity.stateExtra);


        mFirstName = (FloatingLabelEditText)findViewById(R.id.firstNameOwner);
        mLastName = (FloatingLabelEditText)findViewById(R.id.lastName);
        mPermits = (FloatingLabelEditText)findViewById(R.id.permits);
        mBuilding = (FloatingLabelEditText)findViewById(R.id.building);
        mDepartment = (FloatingLabelEditText)findViewById(R.id.department);
        mAddOwner = (FloatingActionButton)findViewById(R.id.addOwnerButton);
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
        //filling in text views with information
        Cursor owner = mParkingDataSource.selectVehicleOwner(ownerID);
        owner.moveToFirst();
            int q = owner.getColumnIndex(ParkingHelper.COLUMN_FIRST_NAME);
            int w = owner.getColumnIndex(ParkingHelper.COLUMN_LAST_NAME);
            int z = owner.getColumnIndex(ParkingHelper.COLUMN_PERMITS);
            int y = owner.getColumnIndex(ParkingHelper.COLUMN_LOCATION);
            int p = owner.getColumnIndex(ParkingHelper.COLUMN_DEPARTMENT);

            mFirstName.setInputWidgetText(owner.getString(q));
            mLastName.setInputWidgetText(owner.getString(w));
            mPermits.setInputWidgetText(owner.getString(z));
            mBuilding.setInputWidgetText(owner.getString(y));
            mDepartment.setInputWidgetText(owner.getString(p));
        checkNull = checkForNull();


        mAddOwner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //check if the entered name is new or another entry in the database
                Cursor owner = mParkingDataSource.selectOwnerByName(mFirstName.getInputWidgetText().toString(),
                        mLastName.getInputWidgetText().toString());
                owner.moveToFirst();
                int z = owner.getColumnIndex(ParkingHelper.COLUMN_ID);
                ownerCheck = owner.getInt(z);
                /*if owner was null when we started check if the new owner name is null.
                if it is null we don't want to search for a null but use current ID
                if it is not null at onclick then we need to check to see if the owner is in the db
                 */
                if (checkNull){

                    if (checkForNull()){
                        //still null add any info to current ownerId
                       updateOwner(ownerID);
                        returnToVi();
                    }
                    else{
                        //not null anymore check if name in db
                        if (owner.getCount() == 0){
                            //no owner by name so use current id and insert in new information
                            updateOwner(ownerID);
                            returnToVi();
                        }
                        else{
                            //owner is in db so use that ownerId and up date the vehicle owner

                            mParkingDataSource.updateVehicleOwner(ownerCheck,mPlate,mState);
                            returnToVi();
                        }

                    }

                }else {
                    /*the text fields are not null then we need to check if there is a entry with name in db
                      if no entry then use current ownerId, if there is a owner in db use that owner Id instead
                     */
                    if (owner.getCount()<= 0 ) {
                        //no owner found so update owner using current id
                        updateOwner(ownerID);

                    }
                    else{
                        if (ownerCheck == ownerID) {
                            //same ID so update current owner
                           updateOwner(ownerID);
                            returnToVi();
                        } else {
                            //different owner so change the vehicle owner
                            mParkingDataSource.updateVehicleOwner(ownerCheck,mPlate,mState);
                            returnToVi();
                        }
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
    protected Boolean checkForNull(){
        if (mFirstName.getInputWidgetText().toString().equals("") && mLastName.getInputWidgetText().toString().equals("")){
            return true;
        }
        else{
            return false;
        }

    }

    protected void updateOwner(int ownerId){
        mOwner = new OwnerData(mFirstName.getInputWidgetText().toString(),
                mLastName.getInputWidgetText().toString(),
                mPermits.getInputWidgetText().toString(),
                mDepartment.getInputWidgetText().toString(),
                mBuilding.getInputWidgetText().toString());
        mParkingDataSource.updateOwner(mOwner, ownerId);

    }

    protected void returnToVi(){
        Intent intent = new Intent(OwnerEdit.this, VehicleInformation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(MainActivity.plateExtra, mPlate);
        intent.putExtra(MainActivity.stateExtra, mState);
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(OwnerEdit.this, VehicleInformation.class);
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
