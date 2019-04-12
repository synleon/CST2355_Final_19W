package com.example.cst2355_final_19w;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dict_SearchDetail extends AppCompatActivity {
    /**
     * declare word to search
     */
    private String searchKeyword;
    /**
     * declare URL
     */
    private String url = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/";
    /**
     * declare key
     */
    private String key = "?key=4556541c-b8ed-4674-9620-b6cba447184f";
    /**
     * declare search URL
     */
    private String fullURL ;
    /**
     * declare  word
     */
    private TextView wordTV;
    /**
     * declare pronunciation
     */
    private TextView pronunciationTV;
    /**
     * declare definition
     */
    private TextView defTV;
    /**
     * declare button to save, delete, back
     */
    private Button saveBtn, deleteBtn, backBtn;
    /**
     *  declare word, pronunciation, definition
     */
    private String word, pronunciation, def = "";
    /**
     * declare progressbar
     */
    private ProgressBar progressBar;
    /**
     * declare ID
     */
    private long wordId;
    /**
     * declare database DBhelper
     */
    Dict_DBHelper dbOpener;
    /**
     *  declare database
     */
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dict_searchdetaillayout);

        wordTV = (TextView)findViewById(R.id.wordTextView);
        pronunciationTV = (TextView)findViewById(R.id.pronunciationTextView);
        defTV = (TextView)findViewById(R.id.defTextView);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        saveBtn.setVisibility(View.GONE);
        deleteBtn = (Button)findViewById(R.id.deleteButton);
        deleteBtn.setVisibility(View.GONE);
        backBtn = (Button)findViewById(R.id.backBtn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        dbOpener= new Dict_DBHelper(this);
        db = dbOpener.getWritableDatabase();

        Intent mainPage = getIntent();

        backBtn.setOnClickListener(b -> {

            Intent back = new Intent(Dict_SearchDetail.this, Dict_MainActivity.class);
            startActivity(back);
                });


    searchKeyword = mainPage.getStringExtra("searchKeyword");
        if (searchKeyword == null){
            wordTV.setText(mainPage.getStringExtra("wordSave"));
            pronunciationTV.setText(mainPage.getStringExtra("proSave"));
            defTV.setText(mainPage.getStringExtra("defSave"));
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(b -> {

                Snackbar sb = Snackbar.make(deleteBtn, "Do you want to delete?", Snackbar.LENGTH_LONG)
                        .setAction("Delete", e -> {
                            int numDeleted = db.delete(Dict_DBHelper.TABLE_NAME, Dict_DBHelper.COL_ID + "=?", new String[] {mainPage.getStringExtra("idSave")});
                            setResult(35);
                            finish();
                        });
                sb.show();


            });


        }else {
            fullURL = url + searchKeyword + key;
            DataFetcher networkThread = new DataFetcher();
            System.out.println(fullURL);
            networkThread.execute(fullURL);
        }

    }

    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class DataFetcher extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String ... params) {


            try {
                //get the string url:
                String myUrl = params[0];

                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();


                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");  //inStream comes from line 46

                int elementIndex = 0;
                int defIndex = 0;
                //now loop over the XML:
                while(xpp.next() != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.next() == XmlPullParser.START_TAG)
                    {
                        String tagName = xpp.getName();

                        if (tagName.equals("entry")){
                            elementIndex++;
                            if (elementIndex >1 )
                                break;
                            if (xpp.getAttributeValue(null,"id").contains("[")){
                                word = xpp.getAttributeValue(null,"id").substring(0, xpp.getAttributeValue(null,"id").indexOf("["));

                            }else {
                                word = xpp.getAttributeValue(null,"id");
                            }
                            publishProgress(25);
                            Thread.sleep(500);
                        }
                        if (tagName.equals("pr")){
                            pronunciation = xpp.nextText();
                            publishProgress(50);
                            Thread.sleep(500);
                        }
                        if (tagName.equals("dt")){
                            if (defIndex > 2){
                                break;
                            }
                            String xmlInner = getInnerXml(xpp);
                            if (xmlInner.contains("<")){
                                xmlInner = xmlInner.substring(0, xmlInner.indexOf("<"));
                            }
                            if (xmlInner.contains(":")){
                                xmlInner = xmlInner.substring(1, xmlInner.length());
                            }
                            def = def + (defIndex+1)+". " +xmlInner + " \n";
                            defIndex++;
                            publishProgress(75);
                            Thread.sleep(500);
                        }
                    }


                }
                publishProgress(100);
                Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin

            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            //return type 3, which is String:
            return "Finished task";
        }

        public  String getInnerXml(XmlPullParser parser)
                throws XmlPullParserException, IOException {
            StringBuilder sb = new StringBuilder();
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        if (depth > 0) {
                            sb.append("</" + parser.getName() + ">");
                        }
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        StringBuilder attrs = new StringBuilder();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            attrs.append(parser.getAttributeName(i) + "=\""
                                    + parser.getAttributeValue(i) + "\" ");
                        }
                        sb.append("<" + parser.getName() + " " + attrs.toString() + ">");
                        break;
                    default:
                        sb.append(parser.getText());
                        break;
                }
            }
            String content = sb.toString();
            return content;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //the parameter String s will be "Finished task" from line 27
            wordTV.setText(word);
            pronunciationTV.setText("Pronunciation: "+pronunciation);
            defTV.setText("Definition: \n"+def);
            saveBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            saveBtn.setOnClickListener( click ->
            {



                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();
                //put string word in the Word column:
                newRowValues.put(Dict_DBHelper.COL_WORD, word);
                //put string pronunciation in the pronunciation column:
                newRowValues.put(Dict_DBHelper.COL_PRONUNCIATION, pronunciation);
                //put string def in the DEF column:
                newRowValues.put(Dict_DBHelper.COL_DEFINITION, def);
                //insert in the database:
                long id  = db.insert(Dict_DBHelper.TABLE_NAME, null, newRowValues);
                wordId = id;



                //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
                Snackbar.make(saveBtn, "Inserted "+word, Snackbar.LENGTH_LONG).show();
                if (id > 0){
                    saveBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.VISIBLE);

                }
            });

            deleteBtn.setOnClickListener(b -> {


                Snackbar sb = Snackbar.make(deleteBtn, "Do you want to delete?", Snackbar.LENGTH_LONG)
                        .setAction("Delete", e -> {
                            int numDeleted = db.delete(Dict_DBHelper.TABLE_NAME, Dict_DBHelper.COL_ID + "=?", new String[] {Long.toString(wordId)});
                            finish();
                        });
                sb.show();


            });

        }
    }
}
