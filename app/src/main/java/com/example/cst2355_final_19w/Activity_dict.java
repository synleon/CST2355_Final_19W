package com.example.cst2355_final_19w;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Activity_dict extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        Toast.makeText(this, "You have entered dictionary page!", Toast.LENGTH_LONG).show();
    }
}
