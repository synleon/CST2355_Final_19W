package com.example.cst2355_final_19w;

import android.content.DialogInterface;
import android.content.Intent;
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

public class Activity_nf_main extends AppCompatActivity
{

    /** declare a variable with type Toolbar used for creating a toolbar object.
     *  @param tBar */
    private Toolbar tBar;

    /** declare a variable with type ArrayList to list the articles found online
     *  @param news */
    //protected static ArrayList<NF_Article> NEWS = new ArrayList<>();

    /** declare several final static variables with type String for using in a database
     *  @param ITEM_SELECTED
     *  @param ITEM_POSITION
     *  @param ITEM_ID
     *  @param db*/
    private static final String ITEM_SELECTED = "ITEM";
    private static final String ITEM_POSITION = "POSITION";
    private static final String ITEM_ID = "ID";
    protected SQLiteDatabase db;
    private Cursor results;

    /** declare two variables with type static final int for startActivityForResult function*/
    private static final int REQUSTCODE = 20;
    private static final int RESULTCODE = 50;

    protected static String SEARCHTERM = null;
   // protected static NewsAdapter ADAPTER;

    //private ProgressBar progressBar;
    //private TextView articalTitle;
    //private TextView url;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Toast.makeText(this,"Welcome to News Feed page", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_mainlayout);

        /** create an object of progress bar and set it visible.*/
        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);

        /** create an object of tool bar and display it.*/
        tBar = (Toolbar)findViewById(R.id.toolbar_newsF);
        setSupportActionBar(tBar);

        /** create an object of NF_DatabaseOpenHelper to open a writable dababase */
        NF_DatabaseOpenHelper dbOpener = new NF_DatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();



        /** set a function for "SEARCH" button. */
        Button searchButton = (Button) findViewById(R.id.sb_newsF);
        searchButton.setOnClickListener( sb ->
            {
                try{
                    EditText editSearchText = (EditText) findViewById(R.id.searchEdit_newsF) ;
                    SEARCHTERM = editSearchText.getText().toString();
                    URLEncoder.encode(SEARCHTERM, "UTF-8");
                } catch(UnsupportedEncodingException e){
                    Log.e("Crash", e.getMessage());
                }

                Intent nextToDo = new Intent(Activity_nf_main.this, Activity_nf_url_connector.class);
                startActivity(nextToDo);

                /*ContentValues newValue = new ContentValues();
                newValue.put(NF_DatabaseOpenHelper.COL_TITLE,);
                long newId = db.insert(NF_DatabaseOpenHelper.TABLE_NAME, null, newValue);
                printCursor();
                typedText.setText("");

                chatMessage.add(new Message(typedMessage, false, 0,newId)); */


                //ADAPTER.notifyDataSetChanged();

                //Snackbar.make(searchBu, "Inserted message id:" + newId, Snackbar.LENGTH_LONG).show();

            });

        /** set a function for "GO BACK" button. */
        Button goBackBu = (Button)findViewById(R.id.goback);
        goBackBu.setOnClickListener( b -> {
            Intent goBackIntent = new Intent(Activity_nf_main.this, MainActivity.class);
            startActivity(goBackIntent);
        });

/*        *//** create an object of listView
         *  then use it to call the function setAdapter() with a parameter "adapter" which is an object of
         *  the inner class called NewsAdapter. *//*
        ListView newsList = (ListView) findViewById(R.id.list_newsF);
        NewsAdapter adapter = new NewsAdapter();
        newsList.setAdapter(adapter);

        newsList.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent nextActivity = new Intent(Activity_nf_main.this, Activity_listDetail_newsfeed.class );
            startActivity(nextActivity);
        });*/

    }

    /** display the items of toolbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_newsfeed, menu);

        return true;
    }

    /** this method is used to define a utility of each item in the toolbar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.dictionary:

                /** go to dictionary section when click the icon*/
                Intent goDictionary = new Intent(Activity_nf_main.this, Activity_dict.class);
                startActivity(goDictionary);
                break;

            case R.id.flightStat_newsFeed:

                /** go to flight state section when click the icon*/
                Intent goFlight = new Intent(Activity_nf_main.this, Activity_flightstatus.class);
                startActivity(goFlight);
                break;

            case R.id.item_NYtime_newsFeed:

                /** show a snack bar when click New York Time icon on toolbar */
                showSnackBar();
                break;

            case R.id.item_help_newsFeed:

                /** make a alert when click overflow title */
                alert();
                break;
        }
        return true;
    }

    /** this method will be executed when the item "HELP" in the toolbar is clicked. */
    public void alert()
    {
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

    /** this method will be executed when the item "New York Times" in the toolbar is clicked. */
    public void showSnackBar()
    {
        /** find view */
        tBar = (Toolbar)findViewById(R.id.toolbar_newsF);

        /** create an object of Snackbar
         *  use it to set a button with the function needed
         *  then show the view*/
        Snackbar sb = Snackbar.make(tBar, "Go to New York Time Article Search page?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        // activity need to be changed
                        Intent nextHop = new Intent(Activity_nf_main.this, Activity_nytimes.class);
                        startActivity(nextHop);
                    }});
        sb.show();
    }

/*    *//** this inner class is used for populating the listView*//*
    protected class NewsAdapter extends BaseAdapter
    {
        public NewsAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return NEWS.size();
        }

        @Override
        public Object getItem(int position) {
            return NEWS.get(position).getTitle();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.activity_nf_single_row_type,parent,false);

            TextView rowText = (TextView) newView.findViewById(R.id.article_title);
            //String textToShow = getItem(position).toString();
            //rowText.setText(textToShow);
            rowText.setText(getItem(position).toString());

            return newView;
        }
    }*/



    public void printCursor() {
        /*Log.e("MyDatabaseFile version:", db.getVersion() + "");
        Log.e("Number of columns:", results.getColumnCount() + "");
        Log.e("Name of the columns:", results.getColumnNames().toString());
        Log.e("Number of results", results.getCount() + "");
        Log.e("Each row of results :", "");*/
        results.moveToFirst();
        for (int i = 0; i < results.getCount(); i++) {
            while (!results.isAfterLast()) {
                boolean isSent = results.getInt(0) > 0;
                String message = results.getString(1);
                long id = results.getLong(2);
                Log.e("id", id + "");
                Log.e("isSent", isSent + "");
                Log.e("message", message + "");
                results.moveToNext();
            }
        }
    }
}
