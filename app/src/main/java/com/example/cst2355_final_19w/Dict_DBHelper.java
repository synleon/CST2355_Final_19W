package com.example.cst2355_final_19w;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Dict_DBHelper extends SQLiteOpenHelper {
    /**
     * declare a database name.
     */

    public static final String DATABASE_NAME = "DictDB";
    /**
     *declare a version number to 1.
     */

    public static final int VERSION_NUM = 1;
    /**
     * declare a string table name
     */

    public static final String TABLE_NAME = "Definition";
    /**
     * declare a string column ID
     */

    public static final String COL_ID = "_id";
    /**
     * declare a string column word
     */

    public static final String COL_WORD = "WORD";
    /**
     * declare a string column pronunciation
     */

    public static final String COL_PRONUNCIATION = "PRONUNCIATION";
    /**
     * declare a string column definition
     */

    public static final String COL_DEFINITION = "DEFINITION";

    /**
     * blank constructor
     */
    public void  Dict_DBHelper(){


    }

    /**
     * constructor with activity
     * @param ctx
     */
    public Dict_DBHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    /**
     * create databae db
     * @param db
     */
    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_WORD + " TEXT,  "+ COL_PRONUNCIATION + " TEXT, " + COL_DEFINITION + " TEXT)");
    }

    /**
     * upgrade database
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
     * downgrade database
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

    //view data
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        return cursor;
    }
}
