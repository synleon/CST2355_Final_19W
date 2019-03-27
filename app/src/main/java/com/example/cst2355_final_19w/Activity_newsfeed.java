package com.example.cst2355_final_19w;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

/** This class is a main class for launch the News Feed section of the final project application.
 *
 *  @Author: Linlin Cheng
 *  @Since: 2019-03-24
 *  @Reference: Professor Eric Torunski, InClassExamples_W19
 */

public class Activity_newsfeed extends AppCompatActivity
{

    /** declare a variable with type Toolbar used for creating a toolbar object. */
    Toolbar tBar;

    // need to be changed
    public String[] news = {"news1", "news2","new3", "new4","new5","news6", "news7","new8", "new9","new10",
            "news11", "news12","new13", "new14","new15"};

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        /** create an object of progress bar and set it visible.*/
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        /** create an object of tool bar and display it.*/
        tBar = (Toolbar)findViewById(R.id.toolbar_newsF);
        setSupportActionBar(tBar);

        /** set a function for "GO BACK" button. */
        Button goBackBu = (Button)findViewById(R.id.goback_newsF);
        goBackBu.setOnClickListener( b -> {
            Intent goBackIntent = new Intent(Activity_newsfeed.this, MainActivity.class);
            startActivity(goBackIntent);
        });

        /** create an object of listView
         *  then use it to call the function setAdapter() with a parameter "adapter" which is an object of
         *  the inner class called NewsAdapter. */
        ListView newsList = (ListView) findViewById(R.id.list_newsF);
        NewsAdapter adapter = new NewsAdapter();
        newsList.setAdapter(adapter);

        LayoutInflater inflater = getLayoutInflater();
        //View newView = inflater.inflate(R.layout.activity_list_newsfeed,parent,false);

        newsList.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent nextActivity = new Intent(Activity_newsfeed.this, Activity_listDetail_newsfeed.class );
            startActivity(nextActivity);
        });

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
                /** make toast when click dictionary icon on toolbar */
                Toast.makeText(this, "You have come to Dictionary Section.", Toast.LENGTH_LONG).show();
                break;
            case R.id.flightStat_newsFeed:
                /** make toast when click flight icon on toolbar */
                Toast.makeText(this, "You have come to Flight Status Section.", Toast.LENGTH_LONG).show();
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
        View helpView = getLayoutInflater().inflate(R.layout.activity_dialogbox_newsfeed, null);

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
        Snackbar sb = Snackbar.make(tBar, "Go to New York Time Article Search?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        // activity need to be changed
                        Intent nextHop = new Intent(Activity_newsfeed.this, MainActivity.class);
                        startActivity(nextHop);
                    }});
        sb.show();
    }

    /** this inner class is used for populating the listView*/
    protected class NewsAdapter extends BaseAdapter
    {
        public NewsAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return news.length;
        }

        @Override
        public Object getItem(int position) {
            return news[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.activity_list_newsfeed,parent,false);

            TextView rowText = (TextView) newView.findViewById(R.id.item);
            //String textToShow = getItem(position).toString();
            //rowText.setText(textToShow);
            rowText.setText(getItem(position).toString());

            return newView;
        }
    }
}
