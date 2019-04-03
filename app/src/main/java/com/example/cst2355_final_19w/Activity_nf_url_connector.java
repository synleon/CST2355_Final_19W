package com.example.cst2355_final_19w;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/** This class is used for connecting the the web "http://webhose.io"  and get useful information
 *  then set the info into a layout file */

public class Activity_nf_url_connector extends AppCompatActivity {
    ProgressBar progressBar;
    EditText searchEditText;
    ImageView icon;
    TextView articleTitle;
    TextView articleUrl;
    TextView articleText;
    NewsAdapter adapter;
    int positionClicked = 0;
    private Toolbar tBar;

    //public static SQLiteDatabase DB;

    public static final String ITEM_SELECTED = "TITLE";
    public static final String ITEM_TEXT = "TEXT";
    public static final String ITEM_URL = "URL";

    public static final int EMPTY_ACTIVITY = 345;

    protected static ArrayList<NF_Article> NEWS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_list_all);

        /** create an object of tool bar and display it.*/
        tBar = (Toolbar)findViewById(R.id.toolbar_newsF);
        setSupportActionBar(tBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        articleTitle = (TextView) findViewById(R.id.titleOfArticle);
        articleUrl = (TextView) findViewById(R.id.urlOfArticle);
        articleText = (TextView) findViewById(R.id.textOfArticle);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //NF_DatabaseOpenHelper dbOpener = new NF_DatabaseOpenHelper(this);
        //DB = dbOpener.getWritableDatabase();

        NFQuery nfQuery = new NFQuery();
        nfQuery.execute("http://webhose.io/filterWebContent?" +
                "token=264a10ac-9a70-4d5b-9f57-280bb2ec5604&format=xml&sort=crawled" + Activity_nf_main.SEARCHTERM);


        ListView newsList = (ListView) findViewById(R.id.list_newsF);
        adapter = new NewsAdapter();
        newsList.setAdapter(adapter);

        boolean isTablet = findViewById(R.id.frame) != null;

        newsList.setOnItemClickListener((parent, view, position, id) -> {
            Log.e("you clicked on :", "item " + position);
            //save the position in case this object gets deleted or updatednew
            positionClicked = position;

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, NEWS.get(position).getTitle());
            dataToPass.putString(ITEM_TEXT, NEWS.get(position).getText());
            dataToPass.putString(ITEM_URL, NEWS.get(position).getUrlAddress());

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
                Intent nextActivity = new Intent(Activity_nf_url_connector.this, Activity_nf_empty.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY);
            } // end of if else
        });
    }

    private class NFQuery extends AsyncTask<String, Integer, String> {
        private final int maxNewsCount = 2;
        private String urlAddress;
        private String title;
        private String text;

        @Override
        protected String doInBackground(String... strings) {
            try {
                parseXMLContent();
            } catch (Exception e) {
                Log.e("Crash", e.getMessage());
            }
            return "Finish all tasks";
        }

        private void parseXMLContent()
                throws MalformedURLException, IOException, XmlPullParserException, InterruptedException {
            /** connect to an url to find the weather info */
            URL url = new URL("http://webhose.io/filterWebContent?token=264a10ac-9a70-4d5b-9f57-280bb2ec5604&format=xml&sort=crawled&q=" + Activity_nf_main.SEARCHTERM);
            HttpURLConnection webHoseConnecter = (HttpURLConnection) url.openConnection();

            webHoseConnecter.setReadTimeout(10000 /* milliseconds */);
            webHoseConnecter.setConnectTimeout(15000 /* milliseconds */);
            webHoseConnecter.setRequestMethod("GET");
            webHoseConnecter.setDoInput(true);

            /** get input stream*/
            InputStream inStream = webHoseConnecter.getInputStream();


            /** create a pull parser use the Factory pattern*/
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(inStream, "UTF-8");

            NEWS.clear();

            /** loop through the xml file*/
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG
                        && xpp.getName().equalsIgnoreCase("post")) {
                    parsePost(xpp);
                }
                xpp.next();
            }
        }

        private void parsePost(XmlPullParser xpp)
                throws IOException, XmlPullParserException, InterruptedException {
            // if there are more than one URL, Title, and Text in this post,
            // only fetch the first
            boolean foundURL = false;
            boolean foundTitle = false;
            boolean foundText = false;

            while (true) {
                xpp.next();
                if (xpp.getEventType() == XmlPullParser.END_DOCUMENT)
                    break;
                else if (xpp.getEventType() == XmlPullParser.END_TAG
                        && xpp.getName().equalsIgnoreCase("post"))
                    break;
                else if (xpp.getEventType() != XmlPullParser.START_TAG)
                    continue;

                String tagName = xpp.getName();

                if (!foundURL && tagName.equals("url")) {
                    foundURL = true;
                    urlAddress = xpp.nextText();
                    Log.e("News Feed ", "Find URL: " + urlAddress);
                    publishProgress(25);
                } else if (!foundTitle && tagName.equals("title")) {
                    foundTitle = true;
                    title = xpp.nextText();
                    Log.e("News Feed ", "Find a title: " + title);
                    publishProgress(50);
                } else if (!foundText && tagName.equals("text")) {
                    foundText = true;
                    text = xpp.nextText();
                    Log.e("News Feed ", "Find the text: " + text);
                    publishProgress(75);

                }
            }

            if (foundTitle && foundText && foundURL
                    && urlAddress != null && title != null && text != null
                    && urlAddress.length() > 0 && title.length() > 0 && text.length() > 0)
                NEWS.add(new NF_Article(title, text, urlAddress));
    }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    protected class NewsAdapter extends BaseAdapter {
        public NewsAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return NEWS.size();
        }

        @Override
        public Object getItem(int position) {
            return NEWS.get(position).getTitle();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.activity_nf_single_row_type, parent, false);

            TextView rowText = (TextView) newView.findViewById(R.id.article_title);
            rowText.setText(getItem(position).toString());

            return newView;
        }
    }

   /* public void deleteMessage(long id, int positionClicked) {
        Log.i("Delete this message:", " id=" + id);
        NEWS.remove(positionClicked);
        Toast.makeText(this, "Message (Id= " + id + ") has been deleted", Toast.LENGTH_LONG).show();
        //If you click the "Delete" button
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[]{Long.toString(id)});
        adapter.notifyDataSetChanged();
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                Log.e("Activity_nf_connector","get the result from Activity_nf_empty");
                *//*long id = data.getLongExtra(ITEM_ID, 0);
                int position = data.getIntExtra(ITEM_POSITION, 0);
                deleteMessage(id, position);*//*
            }
        }
    }*/
}
