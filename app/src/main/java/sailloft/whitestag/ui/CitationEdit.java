package sailloft.whitestag.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marvinlabs.widget.floatinglabel.edittext.FloatingLabelEditText;
import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker;
import com.marvinlabs.widget.floatinglabel.itempicker.ItemPickerListener;
import com.marvinlabs.widget.floatinglabel.itempicker.StringPickerDialogFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import sailloft.whitestag.R;
import sailloft.whitestag.db.ParkingDataSource;
import sailloft.whitestag.db.ParkingHelper;
import sailloft.whitestag.model.CitationsData;

public class CitationEdit extends ActionBarActivity implements ItemPickerListener<String> {
    FloatingLabelItemPicker<String> citeReasonPicker;
    FloatingLabelItemPicker<String> locationPicker;

    FloatingLabelEditText officer;

    FloatingLabelEditText addInfo;
    protected ParkingDataSource mDataSource;
    FloatingActionButton addCite;
    CitationsData mCitationsData;
    protected int[] indices ={};
    private ArrayList<Integer> index = new ArrayList<>();
    private int vehicleId;
    private int ownerId;
    private String mPlate;
    private String mState;
    private String mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citation_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        vehicleId = intent.getIntExtra("vehicleID",0);
        mPlate = intent.getStringExtra("Plate");
        mState = intent.getStringExtra("State");
        mDate = intent.getStringExtra("Date");


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



        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cite = mDataSource.selectCitation(vehicleId, mDate);
        cite.moveToFirst();
        int z = cite.getColumnIndex(ParkingHelper.COLUMN_OFFICER);
        int x = cite.getColumnIndex(ParkingHelper.COLUMN_CITATIONS_TYPE);
        int y = cite.getColumnIndex(ParkingHelper.COLUMN_LOCATION);
        List<String> locAvailable = locationPicker.getAvailableItems();
        int[] locIndex = new int[1];
        locIndex[0] = locAvailable.indexOf(cite.getString(y));
        locationPicker.setSelectedIndices(locIndex);
        officer.setInputWidgetText(cite.getString(z));
        List<String> available = citeReasonPicker.getAvailableItems();

        String citeReason = cite.getString(x);
        citeReason = citeReason.replaceAll("\\[", "").replaceAll("\\]","");
        String[] cites = citeReason.split(",");

        if (cites.length>1) {
            indices = new int[cites.length];


            for (int i = 0; i < cites.length; i++) {

                index.add(available.indexOf(cites[i].trim()));
                indices = convertIntegers(index);


            }

            citeReasonPicker.setSelectedIndices(indices);

        }
        else{
            Log.i("cites:", citeReason + "split:" + cites[0] + "Available:" + available.toString());
            index.add(available.indexOf(cites[0]));
            indices = convertIntegers(index);
            Log.i("CitationsEdit", indices[0] + "");
            citeReasonPicker.setSelectedIndices(indices);
        }









    }
        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(CitationEdit.this,VehicleInformation.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(MainActivity.plateExtra, mPlate);
            intent.putExtra(MainActivity.stateExtra, mState);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_citation_edit, menu);
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
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

}
