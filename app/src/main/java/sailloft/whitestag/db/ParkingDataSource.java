package sailloft.whitestag.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import sailloft.whitestag.model.Vehicle;

/**
 * Created by David's Laptop on 4/23/2015.
 */
public class ParkingDataSource {
    private SQLiteDatabase mDatabase;
    private ParkingHelper mParkingHelper;
    private Context mContext;

    public ParkingDataSource(Context context){
        mContext = context;
        mParkingHelper = new ParkingHelper(mContext);
    }

    //open
    public void open() throws SQLException{
        mDatabase = mParkingHelper.getWritableDatabase();

    }
    //close
    public void close(){
        mDatabase.close();
    }
    //insert

    public void insertVehicle(Vehicle vehicle){
        ContentValues values = new ContentValues();
        values.put(ParkingHelper.COLUMN_ID, vehicle);

    }
    //select
    //update
    //delete

}
