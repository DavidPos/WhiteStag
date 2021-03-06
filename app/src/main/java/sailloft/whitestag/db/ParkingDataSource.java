package sailloft.whitestag.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import sailloft.whitestag.model.CitationsData;
import sailloft.whitestag.model.CiteReasonData;
import sailloft.whitestag.model.LocationData;
import sailloft.whitestag.model.OwnerData;
import sailloft.whitestag.model.SnapShotData;
import sailloft.whitestag.model.VehicleData;

/**
 * Created by David's Laptop on 4/23/2015.
 */
public class ParkingDataSource {
    private SQLiteDatabase mDatabase;
    private ParkingHelper mParkingHelper;
    private Context mContext;
    private String[] allOwnersColumns = {ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_FIRST_NAME, ParkingHelper.COLUMN_LAST_NAME, ParkingHelper.COLUMN_PERMITS, ParkingHelper.COLUMN_LOCATION, ParkingHelper.COLUMN_DEPARTMENT};
    private String[] allSnapShot = {ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_VEHICLE, ParkingHelper.COLUMN_DATE_TIME, ParkingHelper.COLUMN_OWNER, ParkingHelper.COLUMN_LOCATION};
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
    public long insertOwnerReturnId(OwnerData ownerData){
        mDatabase.beginTransaction();
        long row = 0;

        try {
            ContentValues values = new ContentValues();

            values.put(ParkingHelper.COLUMN_FIRST_NAME, ownerData.getFirstName());
            values.put(ParkingHelper.COLUMN_LAST_NAME, ownerData.getLastName());
            values.put(ParkingHelper.COLUMN_PERMITS, ownerData.getPermit());
            values.put(ParkingHelper.COLUMN_LOCATION, ownerData.getWorkLocation());
            values.put(ParkingHelper.COLUMN_DEPARTMENT, ownerData.getDepartment());
            row = mDatabase.insert(ParkingHelper.TABLE_OWNERS, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();

        }

        return row;

    }

    public void insertSnapShot(SnapShotData snapShot) {
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ParkingHelper.COLUMN_OWNER, snapShot.getOwnerId());
            values.put(ParkingHelper.COLUMN_DATE_TIME, snapShot.getDateTime());
            values.put(ParkingHelper.COLUMN_VEHICLE, snapShot.getVehicleId());
            values.put(ParkingHelper.COLUMN_LOCATION, snapShot.getLocation());
            mDatabase.insert(ParkingHelper.TABLE_VEHICLE_SNAPSHOT, null, values);
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

    public void insertLocation(LocationData locationData) {
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(ParkingHelper.COLUMN_LOCATION, locationData.getLocation());
            mDatabase.insert(ParkingHelper.TABLE_LOCATION, null, values);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    public void insertCiteReason(CiteReasonData citeReasonData) {
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(ParkingHelper.COLUMN_CITATIONS_TYPE, citeReasonData.getReason());
            mDatabase.insert(ParkingHelper.TABLE_CITE_REASON, null, values);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }


    //select
    public Cursor selectAllVehicles(){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_VEHICLES,//table
                new String[]{ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_PLATE_NUMBER,ParkingHelper.COLUMN_PLATE_STATE, ParkingHelper.COLUMN_MAKE, ParkingHelper.COLUMN_MODEL, ParkingHelper.COLUMN_OWNER},//column names
                null,//where clause
                null,//where params
                null,//groupby
                null,//having
                null//orderby
                );
        return cursor;


    }
    public Cursor selectAllVehiclesByState(String state){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_VEHICLES,//table
                new String[]{ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_PLATE_NUMBER,ParkingHelper.COLUMN_PLATE_STATE, ParkingHelper.COLUMN_MAKE, ParkingHelper.COLUMN_MODEL, ParkingHelper.COLUMN_OWNER},//column names
                ParkingHelper.COLUMN_PLATE_STATE + " = ?",//where clause
                new String[]{state},//where params
                null,//groupby
                null,//having
                null//orderby
        );
        return cursor;


    }

    public Cursor selectOneVehicleByPlate(String plate, String plateState){

        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_VEHICLES,
                new String[]{ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_PLATE_NUMBER, ParkingHelper.COLUMN_MAKE, ParkingHelper.COLUMN_MODEL, ParkingHelper.COLUMN_YEAR, ParkingHelper.COLUMN_OWNER},
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
                ParkingHelper.TABLE_OWNERS,
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

    public Cursor selectCitation(int vehicleId, String date){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_CITATIONS,
                allCitationColumns,
                ParkingHelper.COLUMN_VEHICLE +" = ?"+
                        " AND " + ParkingHelper.COLUMN_DATE_TIME + " = ?",
                new String[]{Integer.toString(vehicleId), date},
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Cursor selectOwnerByName(String firstName, String lastName){
        Cursor cursor = mDatabase.query(ParkingHelper.TABLE_OWNERS,
                allOwnersColumns,
                ParkingHelper.COLUMN_FIRST_NAME + " = ?" +
                        " AND " + ParkingHelper.COLUMN_LAST_NAME + " = ?",
                new String[]{firstName, lastName},
                null,
                null,
                null,
                null);
        return cursor;

    }
    public Cursor snapShotByVehicleId(int vehicleId){
        Cursor cursor = mDatabase.query(ParkingHelper.TABLE_VEHICLE_SNAPSHOT,
                allSnapShot,
                ParkingHelper.COLUMN_VEHICLE + " = ?",
                new String[]{Integer.toString(vehicleId)},
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Cursor selectAllLocations(){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_LOCATION,
                new String[]{ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_LOCATION},
                null,
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Cursor selectAllCiteReasons(){
        Cursor cursor = mDatabase.query(
                ParkingHelper.TABLE_CITE_REASON,
                new String[]{ParkingHelper.COLUMN_ID, ParkingHelper.COLUMN_CITATIONS_TYPE},
                null,
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }
    //update
    public int updateOwner(OwnerData ownerData, int ownerId){
           ContentValues values = new ContentValues();
            String whereClause = ParkingHelper.COLUMN_ID + " = ?";
            values.put(ParkingHelper.COLUMN_FIRST_NAME, ownerData.getFirstName());
            values.put(ParkingHelper.COLUMN_LAST_NAME, ownerData.getLastName());
            values.put(ParkingHelper.COLUMN_PERMITS, ownerData.getPermit());
            values.put(ParkingHelper.COLUMN_LOCATION, ownerData.getWorkLocation());
            values.put(ParkingHelper.COLUMN_DEPARTMENT, ownerData.getDepartment());
            int rowsUpdated = mDatabase.update(ParkingHelper.TABLE_OWNERS,
                    values,//Values
                    whereClause,//Where
                    new String[]{Integer.toString(ownerId)});//Where params

        return rowsUpdated;



    }
    public int updateVehicle(VehicleData vehicle, String plate, String state){
        ContentValues values = new ContentValues();
        String whereClause = ParkingHelper.COLUMN_PLATE_NUMBER + " = ?" + " AND " + ParkingHelper.COLUMN_PLATE_STATE + " = ?";
        values.put(ParkingHelper.COLUMN_MAKE, vehicle.getMake());
        values.put(ParkingHelper.COLUMN_MODEL, vehicle.getModel());
        values.put(ParkingHelper.COLUMN_PLATE_NUMBER, vehicle.getPlateNumber());
        values.put(ParkingHelper.COLUMN_PLATE_STATE, vehicle.getPlateState());
        values.put(ParkingHelper.COLUMN_OWNER, vehicle.getOwner());
        values.put(ParkingHelper.COLUMN_YEAR, vehicle.getYear());
        int rowsUpdated = mDatabase.update(ParkingHelper.TABLE_VEHICLES,
                values,
                whereClause,
                new String[]{plate, state});
        return rowsUpdated;
    }
    public int updateVehicleOwner(int ownerId, String plate, String state){
        ContentValues values = new ContentValues();
        String whereClause = ParkingHelper.COLUMN_PLATE_NUMBER + " = ?" + " AND " + ParkingHelper.COLUMN_PLATE_STATE + " = ?";
        values.put(ParkingHelper.COLUMN_OWNER, ownerId);
        int rowsUpdated = mDatabase.update(ParkingHelper.TABLE_VEHICLES,
                values,
                whereClause,
                new String[]{plate, state});
        return rowsUpdated;
    }

    public int updateCitation(int vehicleId, String date, String officer, String cite, String location, String addInfo){
        ContentValues values = new ContentValues();
        String whereClause = ParkingHelper.COLUMN_VEHICLE + " = ?" + " AND " + ParkingHelper.COLUMN_DATE_TIME + " = ?";
        values.put(ParkingHelper.COLUMN_OFFICER, officer);
        values.put(ParkingHelper.COLUMN_CITATIONS_TYPE, cite);
        values.put(ParkingHelper.COLUMN_LOCATION, location);
        values.put(ParkingHelper.COLUMN_ADDITIONAL, addInfo);
        int rowsUpdated = mDatabase.update(ParkingHelper.TABLE_CITATIONS,
                values,
                whereClause,
                new String[]{Integer.toString(vehicleId), date});
        return rowsUpdated;

    }

    public int updateLocations(String oldLocation, String newLocation){
        ContentValues values = new ContentValues();
        String whereClause = ParkingHelper.COLUMN_LOCATION + " = ?" ;
        values.put(ParkingHelper.COLUMN_LOCATION, newLocation);
        int rowsUpdated = mDatabase.update(ParkingHelper.TABLE_LOCATION,
                values,
                whereClause,
                new String[]{oldLocation});
        return rowsUpdated;
    }

    public int updateCiteReason(String oldCiteReason, String newCiteReason){
        ContentValues values = new ContentValues();
        String whereClause = ParkingHelper.COLUMN_CITATIONS_TYPE + " = ?" ;
        values.put(ParkingHelper.COLUMN_CITATIONS_TYPE, newCiteReason);
        int rowsUpdated = mDatabase.update(ParkingHelper.TABLE_CITE_REASON,
                values,
                whereClause,
                new String[]{oldCiteReason});
        return rowsUpdated;
    }



    //delete

}
