package com.example.cst2355_final_19w;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class Activity_dict extends AppCompatActivity {

    /**
     * Array adapter
     */
    private WordListAdapter wordListAdapter;
    /**
     * set up default camera capture equal to 1;
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * create progressbar progressBar;
      */
   private  ProgressBar progressBar;
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
    /**
     * create string word;
     */
   private String word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dict);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar_dict);
        setSupportActionBar(bar);

        Toast.makeText(this, "Welcome to Dictionary", Toast.LENGTH_LONG).show();

        Button btn = findViewById(R.id.dict_button);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, "Go back?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });

        Button btn = findViewById(R.id.dict_delete);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, "delete?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });
        (how to connect database to delete a word?)


        ProgressBar progressBar = findViewById(R.id.dict_progressBar);
        progressBar.setProgress(100);


       ListView wordList = findViewById(R.id.dict_list);
       wordListAdapter = new WordListAdapter(this, R.id.dict_list);
        wordList.setAdapter(wordListAdapter);


        sp = getSharedPreferences("Wordlist", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //editor.putString(words);
        editor.putString(wordList);
        editor.commit();
        wordListAdapter.add(word);



         //Setting the item click listener for the listview
          wordList.setOnItemClickListener((parent, container, position, id) -> {
            String word = (String) parent.getItemAtPosition(position);

            (how to display word list here )
        });
    }

    /**
     * A customized array adapter of word
     */
    private class WordListAdapter extends ArrayAdapter<String> {

        /**
         * Inflater used to inflate a layout from the resource
         */
        private LayoutInflater inflater;

        /**
         * constructor
         *
         * @param context
         * @param resource the resource id that identifies the activity
         */
        WordListAdapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        /**
         * Override getView method, used to provide for each item in listview
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = inflater.inflate(R.layout.layout_wordlistitem, null);

            TextView textView = view.findViewById(R.id.textView_word);

            textView.setText(getItem(position));

            return view;
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
                    Toast.makeText(this, "You clicked on the overflow menu",
                            Toast.LENGTH_LONG).show();

                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }

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

        pronounce = (TextView) findViewById(R.id.pronounce);
        type = (TextView) findViewById(R.id.type);
        definition1 = (TextView) findViewById(R.id.definition1);
        definition2 = (TextView) findViewById(R.id.definition2);

        }

    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class DictQuery extends AsyncTask<String, Integer, String> {


    private String id, pronounce, definition1, definition2, type;

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