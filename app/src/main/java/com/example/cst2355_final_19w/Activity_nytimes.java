package com.example.cst2355_final_19w;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Activity_nytimes extends AppCompatActivity {

    private ListView listview;
    private ProgressBar progressBar;
    //private Toolbar tbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nytimes);

        Toast.makeText(this, "Welcome to NY times!", Toast.LENGTH_LONG).show();

       // tbar = findViewById(R.id.newsfeedtoolbar);

    }
}






