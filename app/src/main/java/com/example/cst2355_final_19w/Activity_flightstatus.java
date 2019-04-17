package com.example.cst2355_final_19w;

import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * author: Deyuan Liu
 * flighttrack main activity
 */
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


    private ProgressBar progressBar;
    String icoCode;
   String flight_number;

    //// SQLite database
//    protected static FlightDatabaseOpenHelper chatData;
//    protected SQLiteDatabase db;
//    Cursor results;
//    flighttrackadapter messageAdapter;
//
//    //milestone3
//    public static final String ITEM_SELECTED = "ITEM";
//    public static final String ITEM_TYPE = "TYPE";
//    public static final String ITEM_POSITION = "POSITION";
//    public static final String ITEM_ID = "ID";
//    public static final int EMPTY_ACTIVITY = 345;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightstatus);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_flightstatus);


        setSupportActionBar(mToolbar);


        /**
         * toast message
         */
        Toast.makeText(this, "Welcome to fligth status check page!", Toast.LENGTH_LONG).show();


        ListView flightList = findViewById(R.id.flightstatus_list);

        flightlistAdapter = new flighttrackadapter(this, R.id.flightstatus_list);

        flightList.setAdapter(flightlistAdapter);



        /**
         * progressBar function
         */

        progressBar = findViewById(R.id.flight_progressBar);

        progressBar.setProgress(100);


        /**
         * add click listener function to BUTTONGOTOCHECKFLIGHT button
         */
        Button btn = findViewById(R.id.flightstatus_button);
        btn.setOnClickListener(v -> {

            FlightSearch query = new FlightSearch();

            EditText editText = findViewById(R.id.flightstatus_editText1);
            String airport = editText.getText().toString();
            query.execute(airport);
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
 * show search result alertdialog
 */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("fight Information").setView(detailView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null);
        builder.create().show();
    }

    /**
     * AsyncTask class for searching flight information
     */

    private class FlightSearch extends AsyncTask<String, Eachflight, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                //get the string url:
                String airportCode = params[0];
                //create the network connection
                String serviceUrl = "http://aviation-edge.com/v2/public/flights?key=1054cc-e386ce&arrIata=";
                URL UVurl = new URL( serviceUrl + airportCode);
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                InputStream inStream = UVConnection.getInputStream();


                //create a JSON object from the response
                BufferedReader reader =new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);

                StringBuilder sb =new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //create a JSON object from the result

                JSONArray jsonArray = new JSONArray(result);


                // loop every element
                for (int i = 0; i < jsonArray.length(); ++i) {

                    Log.i("Activity_flightstatus", "doc[" + i + "]");
                    JSONObject item = jsonArray.getJSONObject(i);

                    JSONObject flight = item.getJSONObject("flight");
                    String flightNo = flight.getString("iataNumber");

                    JSONObject departure = item.getJSONObject("departure");
                    String airport = departure.getString("iataCode");


                    Eachflight eachflight = new Eachflight(flightNo, airport);

                    publishProgress(eachflight);

                }

            } catch (Exception ex) {
                Log.e("activity_flightstatus", ex.getMessage());
            }

            return "task done";
        }

        /**
         *
         *
         * @param fname
         * @return
         */

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        /**
         * update GUI
         * @param values
         */
        @Override
        protected void onProgressUpdate(Eachflight... values) {
            Log.i("flightstatus", "update:" + values[0]);
            flightlistAdapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {


         progressBar.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * construct a fligthtrackadapter adapter
     *
     */
    private class flighttrackadapter extends ArrayAdapter<Eachflight> {

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
         * method to provide detail information for each flight
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = inflater.inflate(R.layout.activity_flight_listview, null);
            TextView textViewAirport = view.findViewById(R.id.flight_number);
            TextView textViewFlightCode = view.findViewById(R.id.icoCode);

            textViewAirport.setText(getItem(position).getairport());

            textViewFlightCode.setText(getItem(position).getflightNo());

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

        builder.setMessage(R.string.flight_help)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                         }
                }).setView(helpView);
        builder.create().show();
    }
}




