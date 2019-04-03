package com.example.cst2355_final_19w;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Activity_nf_favorites extends AppCompatActivity
{

    private Cursor results;
    private int titleColIndex;
    private int imageColIndex;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_favorites);

        //results = DB.rawQuery("SELECT " +  NF_DatabaseOpenHelper.COL_TITLE + ", " + NF_DatabaseOpenHelper.COL_IMAGELINK + " FROM " + NF_DatabaseOpenHelper.TABLE_NAME, null);
        results = Activity_nf_main.DB.rawQuery("SELECT * FROM " + NF_DatabaseOpenHelper.TABLE_NAME, null);

        titleColIndex = results.getColumnIndex(NF_DatabaseOpenHelper.COL_TITLE);
        imageColIndex = results.getColumnIndex(NF_DatabaseOpenHelper.COL_IMAGELINK);

        printCursor();

    }

    public void printCursor() {
        Log.e("DatabaseFile version:", Activity_nf_main.DB.getVersion() + "");
        Log.e("Number of columns:", results.getColumnCount() + "");
        Log.e("Name of the columns:", results.getColumnNames().toString());
        Log.e("Number of results", results.getCount() + "");
        Log.e("Each row of results :", "");
        results.moveToFirst();
        for (int i = 0; i < results.getCount(); i++) {
            while (!results.isAfterLast()) {
                String title = results.getString(titleColIndex);
                 //= results.getLong(imageColIndex);
                /*Log.e("id", id + "");
                Log.e("isSent", isSent + "");
                Log.e("message", message + "");*/
            }
        }
    }
}
