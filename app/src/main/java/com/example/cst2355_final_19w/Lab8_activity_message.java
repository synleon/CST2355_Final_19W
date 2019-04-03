package com.example.cst2355_final_19w;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Lab8_activity_message extends Fragment {
    /**
     * create to check is tablet or not
     */
    private boolean isTablet;
    /**
     * create dataFromActivity instance
     */
    private Bundle dataFromActivity;
    /**
     * create message instance
     */
    private String message;
    /**
     * create ID instance
     */
    private long id;
    /**
     * create position instance
     */
    private int position;
    /**
     * create to check is sent or not
     */
    private boolean isSent;


    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(Activity_nytimes.ITEM_ID);
        position = dataFromActivity.getInt("position");
        // position = dataFromActivity.getLong(Activity_nytimes.ITEM_POSITION );
        // isSent = dataFromActivity.getInt(Activity_nytimes.ITEM_ISSEND ) ==1 ? true : false;

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.lab8_2, container, false);

        //show the message
        TextView message = (TextView) result.findViewById(R.id.message);
        //message.setText(dataFromActivity.getString(Activity_nytimes.ITEM_MESSAGE));

        //show the id:
        TextView idView = (TextView) result.findViewById(R.id.idText);
        idView.setText("DatabaseId=" + id + "       Position=" + position + "      isSent=" + isSent);

        // get the delete button, and add a click listener:
        Button deleteButton = (Button) result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(clk -> {

            alertExample();
//

        });

        return result;

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setNegativeButton("message deleted", null);
//                //.setMessage("fight Information").setView(detailView)
//                //.setPositiveButton("OK", null);
//        builder.create().show();
    }

    /**
     * set up dialog box for delete button
     */
    public void alertExample() {
        View middle = getLayoutInflater().inflate(R.layout.dialog, null);


        //btn.setOnClickListener( clk -> et.setText("You clicked my button!"));

        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EmptyActivity parent = (EmptyActivity) getActivity();
                        Intent backToFragmentExample = new Intent();
                        backToFragmentExample.putExtra(Activity_nytimes.ITEM_ID, dataFromActivity.getLong(Activity_nytimes.ITEM_ID));
                        // backToFragmentExample.putExtra(Activity_nytimes.ITEM_POSITION, dataFromActivity.getLong(ChatRoomActivitylab5_lab8.ITEM_POSITION ));
                        parent.setResult(Activity.RESULT_OK, backToFragmentExample);
                        //send data back to FragmentExample in onActivityResult()
                        parent.finish(); //go back
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                        //message = "This is the initial message";
                    }
                }).setView(middle);

        builder.create().show();
    }
}

