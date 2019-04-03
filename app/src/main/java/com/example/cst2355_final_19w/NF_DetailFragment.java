package com.example.cst2355_final_19w;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NF_DetailFragment extends Fragment
{
    private boolean isTablet;
    private int position;
    private Bundle dataFromActivity;

    public void setTablet(boolean tablet)
    {
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_nf_fragment_layout_detail, container, false);

        dataFromActivity = getArguments();

        //show the title
        TextView title = (TextView)result.findViewById(R.id.titleOfArticle);
        title.setText(dataFromActivity.getString(Activity_nf_url_connector.ITEM_SELECTED));

        //show the text:
        TextView text = (TextView)result.findViewById(R.id.textOfArticle);
        text.setText(dataFromActivity.getString(Activity_nf_url_connector.ITEM_TEXT));

        //show the url:
        TextView url = (TextView)result.findViewById(R.id.urlOfArticle);
        url.setText(dataFromActivity.getString(Activity_nf_url_connector.ITEM_URL ));
        url.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        url.setOnClickListener( http ->{
            TextView getUrl = (TextView) result.findViewById(R.id.urlOfArticle);
            getUrl.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String urlHop = url.getText().toString();
            intent.setData(Uri.parse(urlHop));
            startActivity(intent);
        });

        Button saveToFavor = (Button) result.findViewById(R.id.save);
        saveToFavor.setOnClickListener( stf -> {
            ContentValues newValue = new ContentValues();
            newValue.put(NF_DatabaseOpenHelper.COL_TITLE, Activity_nf_url_connector.NEWS.get(position).getTitle());
            newValue.put(NF_DatabaseOpenHelper.COL_TEXT, Activity_nf_url_connector.NEWS.get(position).getText());
            newValue.put(NF_DatabaseOpenHelper.COL_URL,Activity_nf_url_connector.NEWS.get(position).getUrlAddress());
            long newId = Activity_nf_main.DB.insert(NF_DatabaseOpenHelper.TABLE_NAME, null, newValue);
            Snackbar.make( saveToFavor,"Inserted one favorite article which id is : " + newId, Snackbar.LENGTH_LONG).show();
        });

        return result;
    }
}
