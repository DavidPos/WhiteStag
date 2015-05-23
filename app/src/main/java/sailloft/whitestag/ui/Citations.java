package sailloft.whitestag.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.edittext.FloatingLabelEditText;
import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker;
import com.marvinlabs.widget.floatinglabel.itempicker.ItemPickerListener;
import com.marvinlabs.widget.floatinglabel.itempicker.StringPickerDialogFragment;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.model.CitationsData;


public class Citations extends ActionBarActivity implements ItemPickerListener<String> {
    FloatingLabelItemPicker<String> citeReasonPicker;
    FloatingLabelItemPicker<String> locationPicker;

    FloatingLabelEditText officer;

    FloatingLabelEditText addInfo;
    protected ParkingDataSource mDataSource;
    FloatingActionButton addCite;
    CitationsData mCitationsData;
    private int vehicleId;
    private int ownerId;

    public static final String TAG = Citations.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citations);
        Intent intent = getIntent();
        vehicleId = intent.getIntExtra("vehicleID",0);


        officer = (FloatingLabelEditText)findViewById(R.id.officer);

        addInfo = (FloatingLabelEditText)findViewById(R.id.addInfo);
        addCite =(FloatingActionButton)findViewById(R.id.addCiteButton);
        mDataSource = new ParkingDataSource(this);


        citeReasonPicker = (FloatingLabelItemPicker<String>)findViewById(R.id.citeReason);

        citeReasonPicker.setAvailableItems(new ArrayList<String>(Arrays.asList("Item 1.1", "Item 1.2", "Item 1.3", "Item 1.4", "Item 1.5", "Item 1.6", "Item 1.7", "Item 1.8")));
        citeReasonPicker.setWidgetListener(new FloatingLabelItemPicker.OnWidgetEventListener<String>() {
            @Override
            public void onShowItemPickerDialog(FloatingLabelItemPicker<String> source) {
                StringPickerDialogFragment citePicker = StringPickerDialogFragment.newInstance(
                        source.getId(),
                        "Reason For Citation",
                        "OK", "Cancel",
                        true,
                        source.getSelectedIndices(),
                        new ArrayList<>(source.getAvailableItems()));

                citePicker.show(getSupportFragmentManager(), "ItemPicker");
            }
        });
        locationPicker = (FloatingLabelItemPicker<String>)findViewById(R.id.locationPicker);
        String[] location = getResources().getStringArray(R.array.locations);

        locationPicker.setAvailableItems(new ArrayList<>(Arrays.asList(location)));
        locationPicker.setWidgetListener(new FloatingLabelItemPicker.OnWidgetEventListener<String>() {
            @Override
            public void onShowItemPickerDialog(FloatingLabelItemPicker<String> source) {
                StringPickerDialogFragment location = StringPickerDialogFragment.newInstance(
                        source.getId(),
                        "Select Location",
                        "OK", "Cancel",
                        false,
                        source.getSelectedIndices(),
                        new ArrayList<>(source.getAvailableItems()));

                location.show(getSupportFragmentManager(), "Item");
            }
        });

}

    @Override
    public void onCancelled(int pickerId) {
    }

    @Override
    public void onItemsSelected(int pickerId, int[] selectedIndices) {
        if (pickerId == R.id.citeReason) {
            citeReasonPicker.setSelectedIndices(selectedIndices);
            //for() to go through each item in the selected indices array and add to the string

        }
        else if(pickerId == R.id.locationPicker ){
            locationPicker.setSelectedIndices(selectedIndices);
            String mLocations = getResources().getTextArray(R.array.locations)[selectedIndices[0]].toString();
            Log.i(TAG, mLocations );

        }

    }

    @Override
    protected void onResume() {
        super.onResume();




            addCite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    mDataSource.open();

                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.US);



                    String citeReason = citeReasonPicker.getSelectedItems().toString();


                   mCitationsData = new CitationsData(ownerId,
                           officer.getInputWidgetText().toString(),
                           citeReason ,
                           sdf.format(new Date()),
                           0,
                           vehicleId,
                           addInfo.getInputWidgetText().toString(),
                           locationPicker.getInputWidget().getText().toString());
                    mDataSource.insertCitations(mCitationsData);
                        Toast.makeText(Citations.this, "Citation added!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Citations.this,MainActivity.class);
                        startActivity(intent);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });



    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_citations, menu);
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
