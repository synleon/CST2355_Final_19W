package com.example.cst2355_final_19w;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 *
 */

public class Activity_nytimes extends AppCompatActivity {
    /**
     * create the number of objects
     */
    int numObjects = 6;
    /**
     * create ID instance
     */
    public static final String ITEM_ID = "ID";
    /**
     * create the number of EMPTY_ACTTIVITY
     */
    public static final int EMPTY_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nytimes);

         ListAdapter adt = new MyOwnAdapter();


        ListView theList = (ListView)findViewById(R.id.ny_list);

        theList.setAdapter(adt);

        ProgressBar progressBar = findViewById(R.id.ny_progressBar);

        progressBar.setProgress(100);

        Toast.makeText(this, "Welcome to New York Times!", Toast.LENGTH_LONG).show();

        //This listens for items being clicked in the list view
        theList.setOnItemClickListener(( parent,  view,  position,  id) -> {
            Log.e("you clicked on :" , "item "+ position);
            Bundle dataToPass = new Bundle();
            dataToPass.putInt("position", position);
            dataToPass.putLong(ITEM_ID, id);

            Intent nextActivity = new Intent(Activity_nytimes.this, EmptyActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition

           // numObjects = 20;
          //  ((MyOwnAdapter) adt).notifyDataSetChanged();
        });

        Button btn = findViewById(R.id.ny_button);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, "Go back?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });
    }

    //This class needs 4 functions to work properly:
    protected class MyOwnAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return numObjects;
        }

        public Object getItem(int position){
            return "SHow this in row "+ position;
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.activity_save, parent, false );


            TextView rowText = (TextView)newView.findViewById(R.id.textOnRow);
            String stringToShow = getItem(position).toString();
            rowText.setText( stringToShow );
            //return the row:
            return newView;
        }

        public long getItemId(int position)
        {
            return position;
        }
    }

    //A copy of ArrayAdapter. You just give it an array and it will do the rest of the work.
    protected class MyArrayAdapter<E> extends BaseAdapter
    {
        private List<E> dataCopy = null;

        //Keep a reference to the data:
        public MyArrayAdapter(List<E> originalData)
        {
            dataCopy = originalData;
        }

        //You can give it an array
        public MyArrayAdapter(E [] array)
        {
            dataCopy = Arrays.asList(array);
        }


        //Tells the list how many elements to display:
        public int getCount()
        {
            return dataCopy.size();
        }


        public E getItem(int position){
            return dataCopy.get(position);
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            //get an object to load a layout:
            LayoutInflater inflater = getLayoutInflater();

            //Recycle views if possible:
            TextView root = (TextView)old;
            //If there are no spare layouts, load a new one:
            if(old == null)
                root = (TextView)inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            //Get the string to go in row: position
            String toDisplay = getItem(position).toString();

            //Set the text of the text view
            root.setText(toDisplay);

            //Return the text view:
            return root;
        }


        //Return 0 for now. We will change this when using databases
        public long getItemId(int position)
        {
            return 0;
        }
    }
}