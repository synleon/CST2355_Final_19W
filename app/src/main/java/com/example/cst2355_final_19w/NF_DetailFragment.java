package com.example.cst2355_final_19w;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NF_DetailFragment extends Fragment {

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
        View result =  inflater.inflate(R.layout.activity_nf_fragment_layout_detail, container, false);

        //show the title
        TextView title = (TextView)result.findViewById(R.id.titleOfArticle);
        title.setText(dataFromActivity.getString(Activity_nf_url_connector.ITEM_SELECTED));

        //show the text:
        TextView text = (TextView)result.findViewById(R.id.textOfArticle);
        text.setText(dataFromActivity.getString(Activity_nf_url_connector.ITEM_TEXT));

        //show the url:
        TextView url = (TextView)result.findViewById(R.id.urlOfArticle);
        url.setText(dataFromActivity.getString(Activity_nf_url_connector.ITEM_URL));
        return result;
    }
}
