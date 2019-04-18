package com.example.cst2355_final_19w.newsfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cst2355_final_19w.R;

/** this class is used for displaying the searching data passed from Activity_nf_rul_connector
 *  if the device is a phone */
public class Activity_nf_search_empty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from Activity_nf_rul_connector

        NF_Search_DetailFragment dFragment = new NF_Search_DetailFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.empty_frame, dFragment)
                /*.addToBackStack(null)*/
                .commit();
    }
}