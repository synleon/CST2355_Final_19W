package com.example.cst2355_final_19w;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *  This class is a main class for launch the News Feed section of the final project application.
 *
 *  @Author: Linlin Cheng
 *  @Since: 2019-03-24
 *  @Reference: Professor Eric Torunski, InClassExamples_W19
 */

public class Activity_nf_main extends AppCompatActivity {

    /**
     * declare a variable with type Toolbar used for creating a toolbar object.
     *
     * @param tBar
     */
    private Toolbar tBar;

    /** declare a variable with type ArrayList to list the articles found online
     *  @param news */
    //protected static ArrayList<NF_Article> NEWS = new ArrayList<>();

    /**
     * declare several final static variables with type String for using in a database
     *
     * @param ITEM_SELECTED
     * @param ITEM_POSITION
     * @param ITEM_ID
     * @param db
     */
    private static final String ITEM_SELECTED = "ITEM";
    private static final String ITEM_POSITION = "POSITION";
    private static final String ITEM_ID = "ID";
    protected static SQLiteDatabase DB;
    protected static NF_DatabaseOpenHelper OPENHELPER;

    /**
     * declare two variables with type static final int for startActivityForResult function
     */
    private static final int REQUSTCODE = 20;
    private static final int RESULTCODE = 50;

    protected static String SEARCHTERM = null;
    private SharedPreferences prefs;
    private EditText typedSearchTerm;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_mainlayout);

        Toast.makeText(this, "WELCOME TO NEWS FEED PAGE", Toast.LENGTH_LONG).show();

        /** create an object of tool bar and display it.*/
        tBar = (Toolbar) findViewById(R.id.toolbar_newsF);
        setSupportActionBar(tBar);

        OPENHELPER = new NF_DatabaseOpenHelper(this);
        DB = OPENHELPER.getWritableDatabase();

        /** set a function for "SEARCH" button. */
        Button searchButton = (Button) findViewById(R.id.sb_newsF);
        searchButton.setOnClickListener(sb ->
        {
            try {
                EditText editSearchText = (EditText) findViewById(R.id.searchEdit_newsF);
                SEARCHTERM = editSearchText.getText().toString();
                URLEncoder.encode(SEARCHTERM, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e("Crash", e.getMessage());
            }

            Intent nextToDo = new Intent(Activity_nf_main.this, Activity_nf_url_connector.class);
            startActivity(nextToDo);

        });

        typedSearchTerm = (EditText) findViewById(R.id.searchEdit_newsF);
        prefs = getSharedPreferences("SearchTermFile", Context.MODE_PRIVATE);
        String searchTerm = prefs.getString("SearchTerm", "");
        typedSearchTerm.setText(searchTerm);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = prefs.edit();
        String searchTerm = typedSearchTerm.getText().toString();
        editor.putString("SearchTerm",searchTerm);

        // it means the data will be saved in Userinfo.xml file created in onCreate().
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
                Intent goDictionary = new Intent(Activity_nf_main.this, Activity_dict.class);
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
                /** make a list of favorites*/
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
        /** create an object of View used to set the "HELP" layout later*/
        View helpView = getLayoutInflater().inflate(R.layout.activity_nf_helpdialog, null);

        /** create an object of AlertDialog.Builder and
         *  use it to set a message and a button with function needed,
         *  then show the view. */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.help_newsF)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
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
