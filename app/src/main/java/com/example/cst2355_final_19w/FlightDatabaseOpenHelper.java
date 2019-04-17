package com.example.cst2355_final_19w;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FlightDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "Flights";
    public static final String KEY_MESSAGE = "MESSAGE";
    public static final String COL_ID = "_id";
    public static final String COL_flightNo = "flightNo";
    public static final String COL_airport = "airport";
//    public static final String COL_altitude = "altitude";
//    public static final String COL_latitude = "latitude";
//    public static final String COL_longitude = "longitude";
//    public static final String COL_horizontal = "horizontal";
//    public static final String COL_isGround = "isGround";
//    public static final String COL_vertical = "vertical";
//    public static final String COL_status = "status";
    /**
     * create a constructor
     * @param ctx
     */

    public FlightDatabaseOpenHelper(Context ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    /**
     * create flights table
     * @param db
     */
    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_flightNo + " TEXT," + COL_airport + " TEXT)");
    }

    /**
     * this method called when the database upgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     * this method called when the database downgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
