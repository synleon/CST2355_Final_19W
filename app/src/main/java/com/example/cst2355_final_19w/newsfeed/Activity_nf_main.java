package com.example.cst2355_final_19w.newsfeed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import com.example.cst2355_final_19w.Activity_flightstatus;
import com.example.cst2355_final_19w.Dict_MainActivity;
import com.example.cst2355_final_19w.R;
import com.example.cst2355_final_19w.nytimes.Activity_nytimes;

/**
 *
 *  @Author: Linlin Cheng
 *  @Since: 2019-03-24
 *  @Reference: Professor Eric Torunski, InClassExamples_W19
 *
 *  This class is a main class for launch the News Feed section of the final project application.
 */

public class Activity_nf_main extends AppCompatActivity
{
    /** @param tBar and typedSearchTerm used for find view on an activity page */
    private Toolbar tBar;
    private EditText typedSearchTerm;

    /** @param DB used for create a database while OPENHELPER is used for open the database */
    protected static SQLiteDatabase DB;
    protected static NF_DatabaseOpenHelper OPENHELPER;

    /** @param SEARCHTERM used for passing user's searching to an url */
    protected static String SEARCHTERM = null;

    /** @param prefs used for saving search term */
    private SharedPreferences prefs;

    /**
     *
     * @param savedInstanceState
     * This method is used for loading an activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_mainlayout);

        /** set text to a toast.*/
        Toast.makeText(this, "WELCOME TO NEWS FEED PAGE", Toast.LENGTH_LONG).show();

        /** create an object of tool bar and display it.*/
        tBar = (Toolbar) findViewById(R.id.toolbar_newsF);
        setSupportActionBar(tBar);

        /** create an object of NF_DatabaseOpenHelper and use it to open a writable database*/
        OPENHELPER = new NF_DatabaseOpenHelper(this);
        DB = OPENHELPER.getWritableDatabase();

        /** set a function for "SEARCH" button. */
        Button searchButton = (Button) findViewById(R.id.sb_newsF);
        searchButton.setOnClickListener(sb ->
        {
            EditText editSearchText = (EditText) findViewById(R.id.searchEdit_newsF);
            SEARCHTERM = editSearchText.getText().toString();
            //Uri.encode(SEARCHTERM, "UTF-8");

            /*try {
                EditText editSearchText = (EditText) findViewById(R.id.searchEdit_newsF);
                SEARCHTERM = editSearchText.getText().toString();
                URLEncoder.encode(SEARCHTERM, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

            Intent nextToDo = new Intent(Activity_nf_main.this, Activity_nf_url_connector.class);
            startActivity(nextToDo);

        });

        /** get edit text view and set it to the edit text */
        typedSearchTerm = (EditText) findViewById(R.id.searchEdit_newsF);
        prefs = getSharedPreferences("SearchTermFile", Context.MODE_PRIVATE);
        String searchTerm = prefs.getString("SearchTerm", "");
        typedSearchTerm.setText(searchTerm);
    }

    /**
     *  this method will be called when launch other activity
     */
    @Override
    protected void onPause() {
        super.onPause();

        /** save the user's typed words in a file called SearchTermFile in adb shell */
        SharedPreferences.Editor editor = prefs.edit();
        String searchTerm = typedSearchTerm.getText().toString();
        editor.putString("SearchTerm",searchTerm);

        /** it means the data will be saved in the file created in onCreate(). */
        editor.commit();

    }

    /**
     * display the items of toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_newsfeed, menu);

        return true;
    }

    /**
     * this method is used to define a utility of each item in the toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nf_dictionary:

                /** go to dictionary section when click the icon*/
                Intent goDictionary = new Intent(Activity_nf_main.this, Dict_MainActivity.class);
                startActivity(goDictionary);
                break;

            case R.id.nf_flightStat:

                /** go to flight state section when click the icon*/
                Intent goFlight = new Intent(Activity_nf_main.this, Activity_flightstatus.class);
                startActivity(goFlight);
                break;

            case R.id.nf_nytime:

                /** show a snack bar when click New York Time icon on toolbar */
                showSnackBar();
                break;

            case R.id.nf_help:

                /** make a alert when click overflow title */
                alert();
                break;

            case R.id.nf_favorite:
                /** go to favorites page when click favorites icon on toolbar */
                Intent nextToDo = new Intent(Activity_nf_main.this, Activity_nf_favorites.class);
                startActivity(nextToDo);
                break;
        }
        return true;
    }

    /**
     * this method will be executed when the item "HELP" in the toolbar is clicked.
     */

    public void alert() {
        /** create an object of View used to set the "HELP" layout later */
        View helpView = getLayoutInflater().inflate(R.layout.activity_nf_helpdialog, null);

        /** create an object of AlertDialog.Builder and
         *  use it to set a message and a button with function needed,
         *  then show the view. */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // .setMessage(R.string.help_newsF)
        builder.setPositiveButton("GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                    }
                }).setView(helpView);
        builder.create().show();
    }

    /**
     * this method will be executed when the item "New York Times" in the toolbar is clicked.
     */
    public void showSnackBar() {
        /** find view */
        tBar = (Toolbar) findViewById(R.id.toolbar_newsF);

        /** create an object of Snackbar
         *  use it to set a button with the function needed
         *  then show the view*/
        Snackbar sb = Snackbar.make(tBar, "Go to New York Time Article Search page?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // activity need to be changed
                        Intent nextHop = new Intent(Activity_nf_main.this, Activity_nytimes.class);
                        startActivity(nextHop);
                    }
                });
        sb.show();
    }
}
