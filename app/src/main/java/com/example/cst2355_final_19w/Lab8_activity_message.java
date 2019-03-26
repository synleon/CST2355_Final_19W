package com.example.cst2355_final_19w;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Lab8_activity_message extends Fragment {
    private boolean isTablet;
    private Bundle dataFromActivity;
    private String message;
    private long id;
    private long position;
    private boolean isSent;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(Activity_nytimes.ITEM_ID );
       // position = dataFromActivity.getLong(Activity_nytimes.ITEM_POSITION );
       // isSent = dataFromActivity.getInt(Activity_nytimes.ITEM_ISSEND ) ==1 ? true : false;

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.lab8_2, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.message);
        //message.setText(dataFromActivity.getString(Activity_nytimes.ITEM_MESSAGE));

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("DatabaseId=" + id + "       Position=" + position + "      isSent=" + isSent);

        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {


                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(Activity_nytimes.ITEM_ID, dataFromActivity.getLong(Activity_nytimes.ITEM_ID ));
               // backToFragmentExample.putExtra(Activity_nytimes.ITEM_POSITION, dataFromActivity.getLong(ChatRoomActivitylab5_lab8.ITEM_POSITION ));
                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
        });
        return result;
    }
}
