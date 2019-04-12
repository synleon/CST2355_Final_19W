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

public class Dict_Fragment extends Fragment {
    /**
     *  declare a boolean type isTablet
     */
    private boolean isTablet;
    /**
     * declare a bundle to store data
     */
    private Bundle dataFromActivity;
    /**
     * declare string word, pronunciation, definition and ID.
     */
    private String word, pronunciation, def, id;
    /**
     * declare textview word
     */
    private TextView wordTV;
    /**
     * declare textview pronunciation
     */
    private TextView pronunciationTV;
    /**
     * declare Textview definition
     */
    private TextView defTV;


    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();

        word = dataFromActivity.getString("wordSave");
        pronunciation = dataFromActivity.getString("proSave");
        def = dataFromActivity.getString("defSave");
        id = dataFromActivity.getString("idSave");



        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.dict_searchdetaillayout, container, false);


        wordTV = (TextView)result.findViewById(R.id.wordTextView);
        pronunciationTV = (TextView)result.findViewById(R.id.pronunciationTextView);
        defTV = (TextView)result.findViewById(R.id.defTextView);

        wordTV.setText(word);
        pronunciationTV.setText(pronunciation);
        defTV.setText(def);
        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                Dict_MainActivity parent = (Dict_MainActivity)getActivity();
                parent.deleteById(id); //this deletes the item and updates the list


                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
//                EmptyActivity parent = (EmptyActivity) getActivity();
//                Intent backToFragmentExample = new Intent();
//                backToFragmentExample.putExtra(FragmentExample.ITEM_ID, dataFromActivity.getLong(FragmentExample.ITEM_ID ));
//
//                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
//                parent.finish(); //go back
            }
        });
        return result;
    }
}
