package sailloft.whitestag.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David's Laptop on 4/23/2015.
 */
public class ParkingHelper extends SQLiteOpenHelper {
    public static final String TABLE_VEHICLES = "VEHICLES";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_MAKE = "MAKE";
    public static final String COLUMN_MODEL = "MODEL";
    public static final String COLUMN_YEAR = "YEAR";
    public static final String COLUMN_PLATE_NUMBER = "PLATE_NUMBER";
    public static final String COLUMN_OWNER = "OWNER";
    public static final String COLUMN_PLATE_STATE ="PLATE_STATE";
    public static final String COLUMN_ALT_OWNER = "ALT_OWNER";
    private static final String DB_NAME = "parking.db";
    private static final int DB_VERSION = 1;
    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_VEHICLES + " ("+
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MAKE + " STRING, " +
                    COLUMN_MODEL + " STRING, " +
                    COLUMN_YEAR + " INTEGER, " +
                    COLUMN_PLATE_NUMBER + " STRING, " +
                    COLUMN_PLATE_STATE + " STRING, " +
                    COLUMN_OWNER + " STRING, " +
                    COLUMN_ALT_OWNER + " STRING)";


    public ParkingHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
