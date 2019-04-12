package com.example.cst2355_final_19w;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Dict_MainActivity extends AppCompatActivity {
    /**
     * declare a list
     */
    List<Dict_Model> dict_modelArrayList = new ArrayList<>();
    /**
     * declare adapter for listview
     */
    MyOwnAdapter myAdapter;
    /**
     *  declare database dbhelper
     */
    Dict_DBHelper db;
    /**
     *  declare listview
     */
    ListView theList;
    /**
     * decliare textview to show recent search words
     */
    TextView recent;
    /**
     * declare sharedpreferences to store words
     */
    SharedPreferences sp;
    /**
     * declare position to click
     */
    int positionClick = 0;
    /**
     * declare database
     */
    SQLiteDatabase dbAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dict_mainlayout);
        boolean isTablet = findViewById(R.id.fragment) != null; //check if the FrameLayout is loaded
        recent = (TextView) findViewById(R.id.recentWord);
        sp = getSharedPreferences("recentWord", Context.MODE_PRIVATE);
        String savedString = sp.getString("word", "No word");
        recent.setText(savedString);
        // create main toolbar for all the menu entries
        Toolbar toolbar = findViewById(R.id.dict_toolbar);
        setSupportActionBar(toolbar);
        theList = (ListView) findViewById(R.id.the_list);

        ProgressBar progressBar = findViewById(R.id.dict_progressBar);
        progressBar.setProgress(100);

        db = new Dict_DBHelper(this);
        dbAccess = db.getWritableDatabase();
        viewData();

        theList.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString("wordSave", dict_modelArrayList.get(position).getWord());
            dataToPass.putString("proSave", dict_modelArrayList.get(position).getPronunciation());
            dataToPass.putString("defSave", dict_modelArrayList.get(position).getDefinition());
            dataToPass.putString("idSave", (Long.toString(dict_modelArrayList.get(position).getId())));


            if (isTablet) {
                Dict_Fragment dFragment = new Dict_Fragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else {
                Intent emptyActivity = new Intent(this, Dict_SearchDetail.class);
                emptyActivity.putExtras(dataToPass);
                startActivityForResult(emptyActivity, 345);
            }

        });
    }

    private void viewData() {
        Cursor cursor = db.viewData();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Dict_Model dict = new Dict_Model(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getLong(0));
                dict_modelArrayList.add(dict);
                MyOwnAdapter adt = new MyOwnAdapter(dict_modelArrayList, getApplicationContext());
                theList.setAdapter(adt);

            }
        }
    }

    //This class needs 4 functions to work properly:
    protected class MyOwnAdapter extends BaseAdapter {

        private List<Dict_Model> dict_models;
        private Context context;
        private LayoutInflater inflater;

        public MyOwnAdapter(List<Dict_Model> dict_models, Context context) {
            this.dict_models = dict_models;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return dict_models.size();
        }

        @Override
        public Object getItem(int position) {
            return dict_models.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.dict_wordrow, null);

                TextView wordRow = (TextView) view.findViewById(R.id.row_word);
                wordRow.setText(dict_models.get(position).getWord());
            }
            return view;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dict_menu, menu);
        return true;
    }

    /**
     * Toolbar items action
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.help:
                helpDialog();
                break;
            case R.id.search:
                searchDialog();
                break;

            default:
                break;
        }
        return true;
    }


    //help dialog to display author information
    public void helpDialog() {
        View middle = getLayoutInflater().inflate(R.layout.dict_helpdialog, null);
        TextView et = (TextView) middle.findViewById(R.id.dict_helpTextView);
        // et.setText("");
        et.setText(getString(R.string.dict_intro));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Author")

                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }

    //search dialog to enter search and pass the info to another activity
    public void searchDialog() {
        View middle = getLayoutInflater().inflate(R.layout.dict_searchdialog, null);
        EditText et = (EditText) middle.findViewById(R.id.dict_searchEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Search")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (et.getText().toString().length() == 0) {
                            Toast.makeText(getApplicationContext(), "Your input is invalid, enter again to search", Toast.LENGTH_LONG).show();
                        } else {
                            Intent searchDetailPage = new Intent(getApplicationContext(), Dict_SearchDetail.class);
                            searchDetailPage.putExtra("searchKeyword", et.getText().toString());

                            //get an editor object
                            SharedPreferences.Editor editor = sp.edit();

                            //save what was typed under the name "ReserveName"
                            String whatWasTyped = et.getText().toString();
                            editor.putString("word", whatWasTyped);

                            //write it to disk:
                            editor.commit();
                            startActivityForResult(searchDetailPage, 2);

                        }
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If you're coming back from the view contact activity
        System.out.println("result code: " + requestCode);

        sp = getSharedPreferences("recentWord", Context.MODE_PRIVATE);
        String savedString = sp.getString("word", "No word");
        recent.setText(savedString);
        dict_modelArrayList.clear();
        viewData();

    }

    public void deleteById(String id)
    {

        dbAccess.delete(Dict_DBHelper.TABLE_NAME, Dict_DBHelper.COL_ID + "=?", new String[] {id});
        dict_modelArrayList.clear();
        viewData();
    }
}
