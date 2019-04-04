package com.example.cst2355_final_19w;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NF_Favor_DetailFragment extends Fragment
{
    private boolean isTablet;
    private int position;
    private Bundle dataFromActivity;
    private long id;

    public void setTablet(boolean tablet)
    {
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_nf_favor_detail, container, false);

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(Activity_nf_favorites.ITEM_ID);
        position = dataFromActivity.getInt(Activity_nf_favorites.ITEM_POSITION);

        //show the title
        TextView title = (TextView)result.findViewById(R.id.favor_title);
        title.setText(dataFromActivity.getString(Activity_nf_favorites.ITEM_SELECTED));

        //show the text:
        TextView text = (TextView)result.findViewById(R.id.favor_text);
        text.setText(dataFromActivity.getString(Activity_nf_favorites.ITEM_TEXT));

        //show the url:
        TextView url = (TextView)result.findViewById(R.id.favor_url);
        url.setText(dataFromActivity.getString(Activity_nf_favorites.ITEM_URL ));
        url.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        url.setOnClickListener( http ->{
            TextView getUrl = (TextView) result.findViewById(R.id.favor_url);
            getUrl.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String urlHop = url.getText().toString();
            intent.setData(Uri.parse(urlHop));
            startActivity(intent);
        });

        Button deleteButton = (Button) result.findViewById(R.id.delete);
        deleteButton.setOnClickListener( stf -> {
            if(isTablet) { //both the list and details are on the screen:
                Activity_nf_favorites parent = (Activity_nf_favorites)getActivity();
                parent.deleteMessage(id, position); //this deletes the item and updates the list

                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();

            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                Activity_nf_favor_empty parent = (Activity_nf_favor_empty) getActivity();
                Intent backToFavorites = new Intent();
                backToFavorites.putExtra(Activity_nf_favorites.ITEM_ID, id);
                backToFavorites.putExtra(Activity_nf_favorites.ITEM_POSITION, position);

                parent.setResult(Activity.RESULT_OK, backToFavorites); //send data back to ChatroomActivity in onActivityResult()
                parent.finish(); //go back
            }
        });

        return result;
    }
}
