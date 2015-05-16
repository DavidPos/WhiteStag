package sailloft.whitestag.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David's Laptop on 4/23/2015.
 */
public class ParkingHelper extends SQLiteOpenHelper {
    public static final String TABLE_VEHICLES = "VEHICLES";
    public static final String TABLE_OWNERS = "OWNERS";
    public static final String TABLE_CITATIONS ="CITATIONS";
    public static final String TABLE_VEHICLE_SNAPSHOT = "SNAPSHOT";
   //common columns
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_LOCATION = "LOCATION";
    public static final String COLUMN_DATE_TIME = "DATE_TIME";

    public static final String COLUMN_VEHICLE = "VEHICLE";
    //columns for vehicle table
    public static final String COLUMN_MAKE = "MAKE";
    public static final String COLUMN_MODEL = "MODEL";
    public static final String COLUMN_YEAR = "YEAR";
    public static final String COLUMN_PLATE_NUMBER = "PLATE_NUMBER";
    public static final String COLUMN_OWNER = "OWNER";
    public static final String COLUMN_PLATE_STATE ="PLATE_STATE";
    public static final String COLUMN_ALT_OWNER = "ALT_OWNER";
    //columns for owner table
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String COLUMN_LAST_NAME = "LAST_NAME";
    public static final String COLUMN_PERMITS = "PERMITS";
    public static final String COLUMN_DEPARTMENT = "DEPARTMENT";
    //columns for citations table
    public static final String COLUMN_CITATIONS_TYPE = "CITATIONS_TYPE";
    public static final String COLUMN_OFFICER = "OFFICER";
    public static final String COLUMN_ADDITIONAL = "ADDITIONAL";
    public static final String COLUMN_BOOT = "BOOT";


    private static final String DB_NAME = "parking.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE_VEHICLES =
            "CREATE TABLE " + TABLE_VEHICLES + " ("+
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MAKE + " TEXT, " +
                    COLUMN_MODEL + " TEXT, " +
                    COLUMN_YEAR + " INTEGER, " +
                    COLUMN_PLATE_NUMBER + " TEXT, " +
                    COLUMN_PLATE_STATE + " TEXT, " +
                    COLUMN_OWNER + " INTEGER, " +
                    COLUMN_ALT_OWNER + " INTEGER, " + "FOREIGN KEY(" + COLUMN_OWNER +") REFERENCES "+ TABLE_OWNERS +"("+COLUMN_ID+"))";

    private static final String CREATE_TABLE_OWNERS =
            "CREATE TABLE " + TABLE_OWNERS + " ("+
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_PERMITS + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_DEPARTMENT + " TEXT)";

    private static final String CREATE_TABLE_CITATIONS =
            "CREATE TABLE " + TABLE_CITATIONS + " ("+
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CITATIONS_TYPE + " TEXT, " +
                    COLUMN_OWNER + " INTEGER, " +
                    COLUMN_OFFICER + " TEXT, " +
                    COLUMN_BOOT + " INTEGER, " +
                    COLUMN_DATE_TIME + " TEXT, " +
                    COLUMN_VEHICLE + " INTEGER, " +
                    COLUMN_ADDITIONAL + " TEXT, "+
                    COLUMN_LOCATION + " TEXT)";

    private static final String CREATE_TABLE_VEHICLE_SNAPSHOT =
            "CREATE TABLE " + TABLE_VEHICLE_SNAPSHOT + " ("+
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_OWNER + " INTEGER, " +
                    COLUMN_DATE_TIME + " TEXT, " +
                    COLUMN_VEHICLE + " INTEGER, "+
                    COLUMN_LOCATION + " TEXT)";



    public ParkingHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VEHICLES);
        db.execSQL(CREATE_TABLE_OWNERS);
        db.execSQL(CREATE_TABLE_CITATIONS);
        db.execSQL(CREATE_TABLE_VEHICLE_SNAPSHOT);
        if (!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
