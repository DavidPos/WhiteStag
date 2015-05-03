package sailloft.whitestag.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.marvinlabs.widget.floatinglabel.itempicker.FloatingLabelItemPicker;
import com.marvinlabs.widget.floatinglabel.itempicker.ItemPickerListener;
import com.marvinlabs.widget.floatinglabel.itempicker.StringPickerDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

import sailloft.whitestag.R;


public class SnapShot extends ActionBarActivity implements ItemPickerListener<String> {
    FloatingLabelItemPicker<String> locationPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_shot);

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

                location.show(getSupportFragmentManager(), "ItemPicker");
            }
        });
    }

    @Override
    public void onCancelled(int pickerId) {
    }

    @Override
    public void onItemsSelected(int pickerId, int[] selectedIndices) {

            locationPicker.setSelectedIndices(selectedIndices);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_snap_shot, menu);
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
