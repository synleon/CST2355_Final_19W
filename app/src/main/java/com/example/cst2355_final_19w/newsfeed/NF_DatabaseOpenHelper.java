package com.example.cst2355_final_19w.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *  This class is used for creating a database.
 */
public class NF_DatabaseOpenHelper extends SQLiteOpenHelper
{
    /** create several parameters for building a database */
    public final static String TABLE_NAME = "FavoriteNews";
    public final static String DATABASENAME = "NewsFeedDB";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "Title";
    public final static String COL_TEXT = "Text";
    public final static String COL_URL = "URL";
    public final static String COL_IMAGELINK = "ImageLink";

    public static final int VERSION_NUM = 1;

    /** override constructor*/
    public NF_DatabaseOpenHelper(Context ctx)
    {
        super(ctx, DATABASENAME, null,VERSION_NUM );
    }

    /** override method onCreate used for creating database by calling function execSQL() with a SQL statement*/
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_TEXT + " TEXT, "
                + COL_URL + " TEXT,"
                + COL_IMAGELINK + " TEXT);");
    }

    /** override method onDowngrade will be called when the version of database in the constructor
     *  is lower than the one that exists on the device */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /** drop table when it exists and then create a new database*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /** override method onUpgrade will be called when the version of database in the constructor
     *  is newer than the one that exists on the device */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        /** drop table when it exists and then create a new database*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
