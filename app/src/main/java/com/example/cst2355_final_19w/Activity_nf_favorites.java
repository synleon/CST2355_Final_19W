package com.example.cst2355_final_19w;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Activity_nf_favorites extends AppCompatActivity {

    private Cursor results;
    private int titleColIndex;
    private int imageLinkColIndex;
    private int idColIndex;
    private DatabaseAdapter dbAdapter;
    private ArrayList<NF_Article> favorList = new ArrayList<>();
    private ListView favorListView;
    private int positionClicked = 0;

    public static final String ITEM_SELECTED = "TITLE";
    public static final String ITEM_TEXT = "TEXT";
    public static final String ITEM_URL = "URL";

    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";

    public static final int EMPTY_ACTIVITY = 123;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_favorites);

        //results = DB.rawQuery("SELECT " +  NF_DatabaseOpenHelper.COL_TITLE + ", " + NF_DatabaseOpenHelper.COL_IMAGELINK + " FROM " + NF_DatabaseOpenHelper.TABLE_NAME, null);
        results = Activity_nf_main.DB.rawQuery("SELECT * FROM " + NF_DatabaseOpenHelper.TABLE_NAME, null);

        favorListView = (ListView)findViewById(R.id.favor_list);
        dbAdapter = new DatabaseAdapter();
        favorListView.setAdapter(dbAdapter);

        titleColIndex = results.getColumnIndex(NF_DatabaseOpenHelper.COL_TITLE);
        imageLinkColIndex = results.getColumnIndex(NF_DatabaseOpenHelper.COL_IMAGELINK);
        idColIndex = results.getColumnIndex(NF_DatabaseOpenHelper.COL_ID);

        while (results.moveToNext()) {
            String title = results.getString(titleColIndex);
            String imageLink = results.getString(imageLinkColIndex);
            long id = results.getLong(idColIndex);
            favorList.add(new NF_Article(title, imageLink, id));
        }

        favorListView.setOnItemClickListener((parent, view, position, id) -> {
            Log.e("you clicked on :", "item " + position);
            //save the position in case this object gets deleted or updatednew
            positionClicked = position;

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, favorList.get(position).getTitle());
            dataToPass.putString(ITEM_TEXT, favorList.get(position).getText());
            dataToPass.putString(ITEM_URL, favorList.get(position).getUrlAddress());

            boolean isTablet = findViewById(R.id.frame) != null;

            if (isTablet) {
                NF_DetailFragment dFragment = new NF_DetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, dFragment) //Add the fragment in FrameLayout
                        /*.addToBackStack(null)*/ //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(Activity_nf_favorites.this, Activity_nf_empty.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY);
            } // end of if else
        });


    }

    protected class DatabaseAdapter extends BaseAdapter
    {
        public DatabaseAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return favorList.size();
        }

        @Override
        public Object getItem(int position) {
            return favorList.get(position).getTitle();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView;
            LayoutInflater inflater = getLayoutInflater();
            newView = inflater.inflate(R.layout.activity_nf_single_row_type, parent, false);

            TextView favorTitleView = (TextView) newView.findViewById(R.id.article_title);
            favorTitleView.setText(getItem(position).toString());

            return newView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                int position = data.getIntExtra(ITEM_POSITION, 0);
                deleteMessage(id, position);
            }
        }
    }

    // delete the selected message
    public void deleteMessage(long id, int positionClicked)
    {
        Log.i("Deleted one article:" , " id="+id);
        favorList.remove(positionClicked);
        Toast.makeText(this, "One favorite article (Id= " + id + ") has been deleted", Toast.LENGTH_LONG).show();
        //If you click the "Delete" button
        Activity_nf_main.DB.delete(NF_DatabaseOpenHelper.TABLE_NAME, NF_DatabaseOpenHelper.COL_ID + "=?", new String[] {Long.toString(id)});
        dbAdapter.notifyDataSetChanged();
    }
}
