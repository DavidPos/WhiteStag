package sailloft.whitestag.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import sailloft.whitestag.model.CitationsData;
import sailloft.whitestag.model.OwnerData;
import sailloft.whitestag.model.VehicleData;

/**
 * Created by David's Laptop on 4/23/2015.
 */
public class ParkingDataSource {
    private SQLiteDatabase mDatabase;
    private ParkingHelper mParkingHelper;
    private Context mContext;
    private String[] allOwnersColumns = {ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_FIRST_NAME, ParkingHelper.COLUMN_LAST_NAME, ParkingHelper.COLUMN_PERMITS, ParkingHelper.COLUMN_LOCATION, ParkingHelper.COLUMN_DEPARTMENT};
    private String[] allCitationColumns ={ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_CITATIONS_TYPE, ParkingHelper.COLUMN_OWNER,ParkingHelper.COLUMN_OFFICER, ParkingHelper.COLUMN_BOOT, ParkingHelper.COLUMN_DATE_TIME, ParkingHelper.COLUMN_VEHICLE, ParkingHelper.COLUMN_ADDITIONAL, ParkingHelper.COLUMN_LOCATION};
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

    public void insertVehicle(VehicleData vehicle){
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(ParkingHelper.COLUMN_MAKE, vehicle.getMake());
            values.put(ParkingHelper.COLUMN_MODEL, vehicle.getModel());
            values.put(ParkingHelper.COLUMN_PLATE_NUMBER, vehicle.getPlateNumber());
            values.put(ParkingHelper.COLUMN_PLATE_STATE, vehicle.getPlateState());
            values.put(ParkingHelper.COLUMN_OWNER, vehicle.getOwner());
            values.put(ParkingHelper.COLUMN_YEAR, vehicle.getYear());
            mDatabase.insert(ParkingHelper.TABLE_VEHICLES, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }

    }

    public void insertOwner(OwnerData ownerData){
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(ParkingHelper.COLUMN_FIRST_NAME, ownerData.getFirstName());
            values.put(ParkingHelper.COLUMN_LAST_NAME, ownerData.getLastName());
            values.put(ParkingHelper.COLUMN_PERMITS, ownerData.getPermit());
            values.put(ParkingHelper.COLUMN_LOCATION, ownerData.getWorkLocation());
            values.put(ParkingHelper.COLUMN_DEPARTMENT, ownerData.getDepartment());
            mDatabase.insert(ParkingHelper.TABLE_OWNERS, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();

        }


    }

    public void insertCitations(CitationsData citationsData){
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(ParkingHelper.COLUMN_CITATIONS_TYPE, citationsData.getCitationType());
            values.put(ParkingHelper.COLUMN_OWNER, citationsData.getOwner());
            values.put(ParkingHelper.COLUMN_OFFICER, citationsData.getOfficer());
            values.put(ParkingHelper.COLUMN_BOOT, citationsData.getBoot());
            values.put(ParkingHelper.COLUMN_DATE_TIME, citationsData.getTimeDate());
            values.put(ParkingHelper.COLUMN_VEHICLE, citationsData.getVehicle());
            values.put(ParkingHelper.COLUMN_ADDITIONAL, citationsData.getAddInfo());
            values.put(ParkingHelper.COLUMN_LOCATION, citationsData.getLocation());
            mDatabase.insert(ParkingHelper.TABLE_CITATIONS, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }



    //select
    public Cursor selectAllVehicles(){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_VEHICLES,//table
                new String[]{ParkingHelper.COLUMN_PLATE_NUMBER, ParkingHelper.COLUMN_MAKE, ParkingHelper.COLUMN_MODEL, ParkingHelper.COLUMN_OWNER},//column names
                null,//where clause
                null,//where params
                null,//groupby
                null,//having
                null//orderby
                );
        return cursor;


    }

    public Cursor selectOneVehicleByPlate(String plate, String plateState){

        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_VEHICLES,
                new String[]{ParkingHelper.COLUMN_PLATE_NUMBER, ParkingHelper.COLUMN_MAKE, ParkingHelper.COLUMN_MODEL, ParkingHelper.COLUMN_OWNER},
                ParkingHelper.COLUMN_PLATE_NUMBER + " = ?" +
                " AND " + ParkingHelper.COLUMN_PLATE_STATE + " = ?",
                new String[]{plate, plateState},
                null,
                null,
                null,
                null);
            return cursor;


    }

    public Cursor selectVehicleOwner(int ownerId){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_VEHICLES,
                allOwnersColumns,
                ParkingHelper.COLUMN_ID +" = ?",
                new String[]{Integer.toString(ownerId)},
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Cursor selectCitationsForVehicle(int vehicleId){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_CITATIONS,
                allCitationColumns,
                ParkingHelper.COLUMN_VEHICLE +" = ?",
                new String[]{Integer.toString(vehicleId)},
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Cursor selectOwner(int ownerId){
        Cursor cursor = mDatabase.query(ParkingHelper.TABLE_OWNERS,
                null,
                ParkingHelper.COLUMN_ID + " = ?",
                new String[]{Integer.toString(ownerId)},
                null,
                null,
                null,
                null);
        return cursor;

    }
    //update

    //delete

}
