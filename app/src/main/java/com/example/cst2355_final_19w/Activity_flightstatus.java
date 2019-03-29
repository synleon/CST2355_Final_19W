package com.example.cst2355_final_19w;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;




public class Activity_flightstatus extends AppCompatActivity {


    private flighttrackadapter flightlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightstatus);


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
            Snackbar sb = Snackbar.make(btn, "Go back?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });


        /**
         * Add click listener function to listview
         */
        flightList.setOnItemClickListener((parent, container, position, id) -> {
            String airportcode = (String)parent.getItemAtPosition(position);
            chooseflight(airportcode);
        });
    }



    /**
     * method to search airportcode from the listview
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
            flightdetailview.setText("New York speed altitude status" );
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


    /**
     * construct a fligthtrackadapter adapter
     */
    private class flighttrackadapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;


        /**
         * flighttrackadapter constructor
         * @param context
         * @param resource
         */

        flighttrackadapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }


        /**
         *method to provide detial information for each flight
         * @param position this is
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
}









