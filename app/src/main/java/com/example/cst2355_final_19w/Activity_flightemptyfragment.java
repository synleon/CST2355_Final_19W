package com.example.cst2355_final_19w;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Activity_flightemptyfragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightemptyfragment);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
        Activity_flightdetailfragment dFragment = new Activity_flightdetailfragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flight_detail_fragment, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
