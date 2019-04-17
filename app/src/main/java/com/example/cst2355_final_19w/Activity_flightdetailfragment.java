package com.example.cst2355_final_19w;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Activity_flightdetailfragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
//        id = dataFromActivity.getLong(Activity_flightstatus.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_flightdetailfragment, container, false);
//        View view = inflater.inflate(R.layout.activity_flight_listview, null);
//        TextView textViewAirport = view.findViewById(R.id.flight_number);
//        TextView textViewFlightCode = view.findViewById(R.id.icoCode);
//
//        textViewAirport.setText(getItem(position).getairport());
//
//        textViewFlightCode.setText(getItem(position).getflightNo());
//
//        return view;

        //show the message
        TextView message = (TextView)result.findViewById(R.id.message);
//        message.setText(dataFromActivity.getString(Activity_flightstatus.ITEM_SELECTED));

        //show the id:
//        TextView idView = (TextView)result.findViewById(R.id.flight_location);
//        idView.setText("ID=" + id);

            // get the delete button, and add a click listener:
            Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener( clk -> {

                if(isTablet) { //both the list and details are on the screen:
                    Activity_flightstatus parent = (Activity_flightstatus) getActivity();
                   // parent.deleteMessageId((int)id); //this deletes the item and updates the list


                    //now remove the fragment since you deleted it from the database:
                    // this is the object to be removed, so remove(this):
                    parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                }
                //for Phone:
                else //You are only looking at the details, you need to go back to the previous list page
                {
                    Activity_flightemptyfragment parent = (Activity_flightemptyfragment) getActivity();
                    Intent backToFragmentExample = new Intent();
//                    backToFragmentExample.putExtra(Activity_flightstatus.ITEM_ID, dataFromActivity.getLong(Activity_flightstatus.ITEM_ID ));

                    parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                    parent.finish(); //go back
                }
            });
            return result;
        }
    }





