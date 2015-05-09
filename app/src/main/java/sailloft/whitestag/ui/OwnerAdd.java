package sailloft.whitestag.ui;

import android.content.Intent;
import android.database.Cursor;
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
import sailloft.whitestag.db.ParkingHelper;
import sailloft.whitestag.model.OwnerData;

public class OwnerAdd extends ActionBarActivity {
   protected ParkingDataSource mParkingDataSource;
    private FloatingLabelEditText mFirstName;
    private FloatingLabelEditText mLastName;
    private FloatingLabelEditText mPermits;
    private FloatingLabelEditText mBuilding;
    private FloatingLabelEditText mDepartment;
    private FloatingActionButton mAddOwner;
    private OwnerData mOwner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add);

        mFirstName = (FloatingLabelEditText)findViewById(R.id.firstNameOwner);
        mLastName = (FloatingLabelEditText)findViewById(R.id.lastName);
        mPermits = (FloatingLabelEditText)findViewById(R.id.permits);
        mBuilding = (FloatingLabelEditText)findViewById(R.id.building);
        mDepartment = (FloatingLabelEditText)findViewById(R.id.department);
        mAddOwner = (FloatingActionButton)findViewById(R.id.addOwnerButton);
        mParkingDataSource = new ParkingDataSource(this);
        Intent intent = getIntent();
        if(intent != null) {
            mFirstName.setInputWidgetText(intent.getStringExtra("first"));
            mLastName.setInputWidgetText(intent.getStringExtra("last"));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mParkingDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mAddOwner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOwner = new OwnerData(mFirstName.getInputWidgetText().toString(),
                        mLastName.getInputWidgetText().toString(),
                        mPermits.getInputWidgetText().toString(),
                        mDepartment.getInputWidgetText().toString(),
                        mBuilding.getInputWidgetText().toString());
                mParkingDataSource.insertOwner(mOwner);

                Cursor owner = mParkingDataSource.selectOwnerByName(mFirstName.getInputWidgetText().toString(),
                        mLastName.getInputWidgetText().toString());
                owner.moveToFirst();
                int i = owner.getColumnIndex(ParkingHelper.COLUMN_ID);
                int ownerId = owner.getInt(i);

                Intent intent = new Intent(OwnerAdd.this, VehicleAdd.class);

                intent.putExtra("ownerId", ownerId);
                setResult(RESULT_OK, intent);
                finish();


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mParkingDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owner_add, menu);
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
