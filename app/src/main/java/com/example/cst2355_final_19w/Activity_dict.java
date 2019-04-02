package com.example.cst2355_final_19w;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Activity_dict extends AppCompatActivity {

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ITEM_WORD = "WORD";
    public static final int EMPTY_ACTIVITY = 345;

    public static final String ITEM_DEF = "Definition";
    ArrayList<Word> definitions = new ArrayList<>();
    public static final String ITEM_POSITION_DEF = "POSITION";
    public static final String ITEM_ID_DEF = "ID";


    int numObjects = 6;
    ArrayList<Word> words = new ArrayList<>();
    private static int ACTIVITY_VIEW_CONTACT = 33;
    int positionClicked = 0;
    Cursor results;


    /**
     * set up default camera capture equal to 1;
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * create progressbar progressBar;
     */
    private ProgressBar progressBar;
    /**
     * create progress bar progressBar;
     */
    private TextView textView;
    /**
     * create text veiw textView;
     */
    private SQLiteDatabase db;
    /**
     * create sharedpreferences sp;
     */
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dict);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar_dict);
        setSupportActionBar(bar);

        ListView theList = (ListView) findViewById(R.id.dict_list);

        Toast.makeText(this, "Welcome to Dictionary", Toast.LENGTH_LONG).show();

        Button btn = findViewById(R.id.dict_button);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, "Go back?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });

        Button delete_btn = findViewById(R.id.dict_delete);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, "delete?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });

        ProgressBar progressBar = findViewById(R.id.dict_progressBar);
        progressBar.setProgress(100);


        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        //get a database:
        dict_MyDatabaseOpenHelper dbOpener = new dict_MyDatabaseOpenHelper(this);
        //SQLiteDatabase db = dbOpener.getWritableDatabase();
        db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String[] columns = {dict_MyDatabaseOpenHelper.COL_ID, dict_MyDatabaseOpenHelper.COL_word};
        results = db.query(false, dict_MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int wordColIndex = results.getColumnIndex(dict_MyDatabaseOpenHelper.COL_word);
        int idColIndex = results.getColumnIndex(dict_MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String word = results.getString(wordColIndex);
            long id = results.getLong(idColIndex);

            //add the new word to the array list:
            words.add(new Word(word, id));
        }

        ListAdapter adt = new ListAdapter();
        theList.setAdapter(adt);

        //This listens for items being clicked in the list view
        //theList.setOnItemClickListener((parent, view, position, id) -> {
        theList.setOnItemClickListener((list, word) -> {

                    Bundle dataToPass = new Bundle();
                    dataToPass.putString(dict_definitionDatabaseOpenHelper.COL_DEF, String.valueOf(words.getDefinitions()));
                    dataToPass.putString(COL_DEF, words.getDefinition());
            //  dataToPass.putLong(ITEM_POSITION_DEF, position);
            // dataToPass.putLong(ITEM_ID_DEF,definitions .get(position).getId());

                    if (isTablet) {
                        dict_search_fragment dFragment = new dict_search_fragment(); //add a DetailFragment
                        dFragment.setArguments(dataToPass); //pass it a bundle for information
                        dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                                .addToBackStack("AnyName") //make the back button undo the transaction
                                .commit(); //actually load the fragment.
                    } else //isPhone
                    {
                        Intent nextActivity = new Intent(Activity_dict.this, EmptyActivity.class);
                        nextActivity.putExtras(dataToPass); //send data to next activity
                        startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
                    }
                }

        );
              printCursor();
    }

    //This class needs 4 functions to work properly:
    protected class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return words.size();
        }

        public Object getItem(int position) {
            return words.get(position).getWord();//"\nItem "+ (position+1) + "\nSub Item "+ (position+1) +"\n";
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView;
            newView = inflater.inflate(R.layout.dict_search, parent, false);

            TextView rowText = (TextView) newView.findViewById(R.id.dict_editText1);
            String stringToShow = getItem(position).toString();
            rowText.setText(stringToShow);
            return newView;
        }

        public long getItemId(int position) {
            return position;
        }
    }


    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                long position = data.getLongExtra(ITEM_POSITION, 0);
                deleteMessageId((int) id, (int) position);
            }
        }
    }

    public void deleteMessageId(int id, int position) {
        Log.i("Delete this message:", " id=" + id);
        words.remove(position);
/*        for(int i=0; i< messages.size(); i++){
            if(messages.get(i).getId() == id ){
                messages.remove(i);
                i=messages.size();
            }
        }*/
        // messages.remove(new Message(messages.get(id).getMessage(), messages.get(id).getIsSent(), id));
//If you click the "Delete" button
        int numDeleted = db.delete(dict_MyDatabaseOpenHelper.TABLE_NAME,
                dict_MyDatabaseOpenHelper.COL_ID + "=?", new String[]{Long.toString(id)});

        //Log.i("ViewContact", "Deleted " + numDeleted + " rows");

        //set result to PUSHED_DELETE to show clicked the delete button
        //setResult(PUSHED_DELETE);
        //go back to previous page:
        //finish();

        adt.notifyDataSetChanged();
    }


    public void printCursor() {
        Log.e("MyDatabaseFile version:", db.getVersion() + "");
        Log.e("Number of columns:", results.getColumnCount() + "");
        Log.e("Name of the columns:", results.getColumnNames().toString());
        Log.e("Number of results", results.getCount() + "");
        Log.e("Each row of results :", "following");
        results.moveToFirst();
        for (int i = 0; i < results.getCount(); i++) {
            while (!results.isAfterLast()) {
                boolean isSent = results.getInt(1) > 0;
                String message = results.getString(2);
                long id = results.getLong(0);
                String eachLine = "id: " + id + " , isSent: " + isSent + ", message:" + message;
                Log.e("Each row", eachLine + "");
                results.moveToNext();
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dict_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                helpDialog();
                return true;

            case R.id.search:
                search();
                return true;

            case R.id.choice4:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void helpDialog() {
        View helpView = getLayoutInflater().inflate(R.layout.dict_help, null);
        TextView textViewIntro = helpView.findViewById(R.id.textView_intro);
        TextView textViewInstruction = helpView.findViewById(R.id.instruction);

        textViewIntro.setText("Dictionary.com which was invented by Disken in 1965, is the world’s leading online source for English definitions, synonyms, word origins,audio pronunciations, example sentences, slang phrases, idioms, word games, legal and medical terms, Word of the Day and more. V 5.5 is the latest version.");
        textViewInstruction.setText("How to use the dictionary? 1. Browse the dictionary. 2. Searching the dictionary using the Quick Search. 3. Search the dictionary using the Advanced Search. 4. Categories. 5. Sources. 6. Historical Thesaurus. 7. Other resources.");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    public void search() {

        setContentView(R.layout.dict_search);

        DictQuery networkThread = new DictQuery();
        //this starts doInBackground on other thread
        networkThread.execute("https://www.dictionaryapi.com/api/v1/references/sd3/xml/pasta?key=4556541c-b8ed-4674-9620-b6cba447184f");

    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class DictQuery extends AsyncTask<String, Integer, String> {


    private int id, String definition;

        @Override
        protected String doInBackground(String... params) {

                //get the string url:
                String myUrl = params[0];

                //create the network connection:
            URL url = null;
            try {
                url = new URL(myUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                InputStream stream = downloadUrl(myUrl);

                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parseXpp = factory.newPullParser();
                parseXpp.setInput(inStream, "UTF-8");

                //now loop over the XML:
                while (parseXpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parseXpp.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = parseXpp.getName(); //get the name of the starting tag: <tagName>
                        if (tagName.equals("entry")) {
                            String parameter = parseXpp.getAttributeValue(null, "pasta");
                            Log.e("AsyncTask", "pasta " + parameter);
                            publishProgress(1); //tell android to call onProgressUpdate with 1 as parameter
                        }
                        if (tagName.equals("entry")) {
                            id = parseXpp.getAttributeValue(null, "value");
                            Log.e("AsyncTask", "pronounce: " + pronounce);
                            publishProgress(25);
                        }
                        if (tagName.equals("type")) {
                            type = parseXpp.getAttributeValue(null, "icon");
                            Log.e("AsyncTask", "Type: " + type);
                            publishProgress(1); //tell android to call onProgressUpdate with 1 as parameter
                        } else if (tagName.equals("definition")) {
                            definition1 = parseXpp.getAttributeValue(null, "value");
                            Log.e("AsyncTask", "definition: " + definition1);
                            publishProgress(25);

                            definition2 = parseXpp.getAttributeValue(null, "min");
                            Log.e("AsyncTask", "definition: " + definition2);
                            publishProgress(50);
                        }
                    }
                    //parseXpp.next(); //advance to next XML event parseXpp.next(); //advance to next XML event
                }    //End of XML reading

                publishProgress(100);
                Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin
            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.toString() );
            }
            //return type 3, which is String:
            return "Finished task";
        }
                @Override
                protected void onProgressUpdate (Integer...values){
                    Log.i("AsyncTaskExample", "update:" + values[0]);
                    pronounce.setText("At step:" + values[0]);

                }

                @Override
                protected void onPostExecute (String s){
                    //the parameter String s will be "Finished task" from line 27
                    pronounce.setText(pronounce);
                    type.setText(type );
                    definition1.setText(definition1 );
                    definition2.setText(definition2 );
                    textView.setText("");
                }

                public XmlPullParser parse (InputStream in) throws
                        XmlPullParserException, IOException {
                    try {
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        parser.setInput(in, "UTF-8");
                        parser.nextTag();
                        //return readFeed(parser);
                        return parser;
                    } finally {
                        //in.close();
                    }
                }


                // Given a string representation of a URL, sets up a connection and gets
// an input stream.
                private InputStream downloadUrl (String urlString) throws IOException {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    return conn.getInputStream();
                }
                public boolean fileExistance (String fname){
                    File file = getBaseContext().getFileStreamPath(fname);
                    return file.exists();
                }




    //get a database:
    dict_MyDatabaseOpenHelper dbOpener = new dict_MyDatabaseOpenHelper(this);
    //SQLiteDatabase db = dbOpener.getWritableDatabase();
    SQLiteDatabase db = dbOpener.getWritableDatabase();

    //query all the results from the database:
    String[] columns = {dict_MyDatabaseOpenHelper.COL_ID, dict_MyDatabaseOpenHelper.pronounce, dict_MyDatabaseOpenHelper.type};
     results = db.query(false, dict_MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
/*
    //find the column indices:
    int isSentColumnIndex = results.getColumnIndex(dict_MyDatabaseOpenHelper.definition1);
    int messageColIndex = results.getColumnIndex(dict_MyDatabaseOpenHelper.definition2);
    int idColIndex = results.getColumnIndex(dict_MyDatabaseOpenHelper.COL_ID);

    //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
        boolean isSent = results.getInt(isSentColumnIndex) > 0;
        String message = results.getString(messageColIndex);
        long id = results.getLong(idColIndex);

        //add the new Contact to the array list:
        messages.add(new Message(message, isSent, id));
    }
    adapter = new ChatAdapter();
        theList.setAdapter(adapter);


        /*SwipeRefreshLayout refresher = (SwipeRefreshLayout) findViewById(R.id.refresher);
        refresher.setOnRefreshListener(() -> {
            numObjects *= 2;
            ((MyOwnAdapter) adt).notifyDataSetChanged();
            refresher.setRefreshing(false);
        });
        //This listens for items being clicked in the list view
        theList.setOnItemClickListener((parent, view, position, id) -> {
            Log.e("you clicked on :", "item " + position);
            numObjects = 20;
            ((MyOwnAdapter) adt).notifyDataSetChanged();
        });
*/

        sendButton.setOnClickListener(c -> {
        String userType = userTyped.getText().toString();

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string name in the NAME column:
        newRowValues.put(MyDatabaseOpenHelper.COL_ISSENT, true);
        //put string email in the EMAIL column:
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, userType);
        //insert in the database:
        long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
        this.printCursor();
        userTyped.setText("");
        messages.add(new Message(userType, true, newId));
        adapter.notifyDataSetChanged();
        Snackbar.make(sendButton, "Inserted message id:" + newId, Snackbar.LENGTH_LONG).show();
    });

        }



}