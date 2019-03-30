package com.example.cst2355_final_19w;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NFDatabaseOpenHelper extends SQLiteOpenHelper
{
    public final static String TABLE_NAME = "NewsFeed";
    public final static String FILENAME = "NewsFeedDB";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "Title";
    public final static String COL_TEXT = "Text";
    public final static String COL_URL = "URL";
    public static final int VERSION_NUM = 1;

        public NFDatabaseOpenHelper(Activity ctx){
        super(ctx, "NewsFeedDB", null,VERSION_NUM );
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_TEXT + " TEXT, "
                + COL_URL + " TEXT);");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
