package com.example.cst2355_final_19w;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Dict_EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dict_framelayout);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
        Dict_Fragment dFragment = new Dict_Fragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
