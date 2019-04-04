package com.example.cst2355_final_19w;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Activity_flightstatus extends AppCompatActivity {

    private Toolbar mToolbar;
    private View middle;
    private EditText et;
    private Dialog dialog;
    private Button n;
    private Button p;
    private Snackbar sb;
    private String message;
    private flighttrackadapter flightlistAdapter;

    private TextView icoCode;
    private TextView flight_number;


    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightstatus);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_flightstatus);

        //message = "You clicked on the overflow menu";
        setSupportActionBar(mToolbar);

        /**
         * toast message
         */
        Toast.makeText(this, "Welcome to fligth status check page!", Toast.LENGTH_LONG).show();


        ListView flightList = findViewById(R.id.flightstatus_list);

        flightlistAdapter = new flighttrackadapter(this, R.id.flightstatus_list);

        flightList.setAdapter(flightlistAdapter);

        /**
         * add items to flightlist
         */

        flightlistAdapter.add("Ottawa-Montreal");

        flightlistAdapter.add("Toronto-Montreal");

        flightlistAdapter.add("NEW York-Beijing");


        /**
         * progressBar function
         */

        ProgressBar progressBar = findViewById(R.id.flight_progressBar);

        progressBar.setProgress(100);

        /**
         * add click listener function to Button
         */
        Button btn = findViewById(R.id.flightstatus_button);
        btn.setOnClickListener(v -> {
//            Snackbar sb = Snackbar.make(btn, "Go back?", Snackbar.LENGTH_LONG)
//                    .setAction("Yes", e -> finish());
//            sb.show();

            flightsearch search = new flightsearch();
            search.execute();
        });


        /**
         * Add click listener function to listview
         */
        flightList.setOnItemClickListener((parent, container, position, id) -> {
            String airportcode = (String) parent.getItemAtPosition(position);
            chooseflight(airportcode);
        });
    }


    /**
     * method to search airportcode from the listview
     *
     * @param airportcode
     */
    public void chooseflight(String airportcode) {
        View detailView = getLayoutInflater().inflate(R.layout.layout_flightdetail, null);
        TextView flightdetailview = detailView.findViewById(R.id.airport_detail);
        TextView flightview = detailView.findViewById(R.id.airport_code);

/**
 * search airportcode function
 */
        flightview.setText(airportcode);
        if (airportcode.equals("Ottawa-Montreal")) {
            flightdetailview.setText("ottawa speed altitude status");
        } else if (airportcode.equals("Toronto-Montreal")) {
            flightdetailview.setText(" Toronto speed altitude status ");
        } else if (airportcode.equals("NEW York-Beijing")) {
            flightdetailview.setText("New York speed altitude status");
        } else if (airportcode.equals("null")) {
            flightdetailview.setText("the flight is not exist");
        }

/**
 * show search result alertdialog
 */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("fight Information").setView(detailView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null);
        builder.create().show();
    }


    private class flightsearch extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                //get the string url:
                // Strg myUrl = params[0];

                URL UVurl = new URL("http://aviation-edge.com/v2/public/flights?key=f76ac6-220e2a&arrIata=YOW");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                InputStream inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                //JSONObject jObject = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(result);

                // loop every document
                for (int i = 0; i < jsonArray.length(); ++i) {

                    Log.i("Activity_flightstatus", "doc[" + i + "]");
                    JSONObject item = jsonArray.getJSONObject(i);

                    JSONObject flight = item.getJSONObject("flight");
                    String flightNo = flight.getString("iataNumber");

                    JSONObject departure = item.getJSONObject("departure");
                    String airport = departure.getString("")

//
//                    String arrival = doc.getString("arrival.iataCode");
//                    Log.i("Activity_flightstatus", "\tWeb_url=[" + webUrl + "]");
//
//                    String flightnumber = doc.getString("flight.number");
//                    Log.i("Activity_flightstatus", "\tflightnumber=[" + pubDate + "]");
//
//                    // create a flightarriving information object
//                    flightArrivingInformation arrivingFlight = new flightArrivingInformation(arrival, flightnumber);

                    // send the article object to UI thread
                    //publishProgress(arrivingFlight);
                }



            } catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }


            return "finished task";
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("WeatherForcast", "update:" + values[0]);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            //the parameter String s will be "Finished task" from line 27

            // messageBox.setText("Finished all tasks");

            icoCode.setText("Min Temperature:" + icoCode);
            flight_number.setText("flight_number:" + flight_number);


            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * construct a fligthtrackadapter adapter
     */
    private class flighttrackadapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;


        /**
         * flighttrackadapter constructor
         *
         * @param context
         * @param resource
         */

        flighttrackadapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }


        /**
         * method to provide detial information for each flight
         *
         * @param position    this is
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = inflater.inflate(R.layout.layout_flightdetail, null);
            TextView textView = view.findViewById(R.id.airport_code);

            textView.setText(getItem(position));

            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.choice1:
                Toast.makeText(this, "flight landing", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Activity_flightstatus.this, Activity_landing.class));
                break;

            case R.id.choice2:
                Toast.makeText(this, "flight leaving", Toast.LENGTH_SHORT).show();
                break;

            case R.id.choice3:

                dialogFunction();
                break;


        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_flightmenu, menu);
        return true;
    }


    /**
     * toolbar third image clicked, the following method execute.
     */

    public void dialogFunction() {


        View helpView = getLayoutInflater().inflate(R.layout.activity_dialogbox_flightstatus, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.flightstatus_help)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                         }
                }).setView(helpView);
        builder.create().show();
    }
}




