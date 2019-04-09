package com.example.cst2355_final_19w.nytimes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cst2355_final_19w.R;

public class EmptyContainerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nytimes_article_container);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
        Fragment_nytimes_article dFragment = new Fragment_nytimes_article();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nytimes_fragmentLocation, dFragment)
                .commit();
    }
}