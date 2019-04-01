package com.example.cst2355_final_19w.nytimes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
//import android.widget.SearchView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.cst2355_final_19w.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The main activity of new york times page
 * @author leon
 * @since March 30 2019
 *
 */
public class Activity_nytimes extends AppCompatActivity {

    /**
     * Ny times query service URL
     */
    public static final String nytimesUrl       = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    /**
     * API KEY for ny times query service
     */
    public static final String nytimesAPIKEY    = "gfcaESExVhXwfKaOMJUFHJyk428mm6pU";

    /**
     *  Customized Database Helper
     */
    private MyDatabaseOpenHelper dbOpener;

    /**
     *  SQLite database
     */
    private SQLiteDatabase db;

    /**
     * create ID instance
     */
    public static final String ITEM_ID = "ID";
    /**
     * create the number of EMPTY_ACTTIVITY
     */
    public static final int EMPTY_ACTIVITY = 345;

    /**
     * the adapter for articles list view
     */
    private MyListAdapter adapter;

    /**
     * the progress bar
     */
    ProgressBar progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ny_main_toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nytimes);

        // make toast message
        Toast.makeText(this, "Welcome to New York Times!", Toast.LENGTH_LONG).show();

        // initialize customized adapter
        adapter = new MyListAdapter(this, R.id.ny_list);

        ListView theList = findViewById(R.id.ny_list);

        // set adapter to listview
        theList.setAdapter(adapter);

        // initialize progressbar
        progressBar = findViewById(R.id.ny_progressBar);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();


        // Initialize toolbar
        Toolbar toolbar = findViewById(R.id.ny_main_toolbar);
        setSupportActionBar(toolbar);

        //This listens for items being clicked in the list view
        theList.setOnItemClickListener((parent, view, position, id) -> {
            Log.i("you clicked on :", "item " + position);

            NytimesArticleData article = (NytimesArticleData) parent.getItemAtPosition(position);

            Intent nextActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getArticle_url()));
            startActivity(nextActivity);
        });

        // set return button onclick listener
        Button btn = findViewById(R.id.ny_button);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, "Go back?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });

        SearchView searchView = findViewById(R.id.search_nytimes);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.isEmpty()) {
                    return false;
                }
                else {
                    // start async task to perform the article search on new york times
                    // form full query request url
                    String queryString = nytimesUrl + "?api-key=" + nytimesAPIKEY + "&q=" + query.replace(' ', '+');

                    // add log info
                    Log.i("NYTIMES", "QUERY URL=[" + queryString + "]");

                    // let go the query task
                    NytimesArticleQuery articleQuery = new NytimesArticleQuery();

                    // let progress bar appear
                    progressBar.setVisibility(View.VISIBLE);

                    articleQuery.execute(queryString);
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.ny_help_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(R.layout.layout_nytimes_help).setPositiveButton("OK", null);
                builder.create().show();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Method used to save articles to SQLite database
     *
     * @param view the view that user clicked save button, can be used to change how the view looks like after clicked
     * @param article article needs to be saved
     */
    public void saveArticle(View view, NytimesArticleData article) {
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(MyDatabaseOpenHelper.COL_ID, article.getArticle_id());
        newRowValues.put(MyDatabaseOpenHelper.COL_PUBDATE, article.getArticle_pubData());
        newRowValues.put(MyDatabaseOpenHelper.COL_TITLE, article.getArticle_title());
        newRowValues.put(MyDatabaseOpenHelper.COL_AUTHOR, article.getArticle_byline());
        newRowValues.put(MyDatabaseOpenHelper.COL_URL, article.getArticle_url());
        long id = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
        String message = (id != -1) ? "Article saved successfully!" : "Save article failed!";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Button btn = view.findViewById(R.id.ny_article_save);
        btn.setText("Saved!");
        btn.setEnabled(false);
    }

    /**
     * AsyncTask class performing New York Times article search
     * @author leon
     * @since Mar 30 2019
     */
    private class NytimesArticleQuery extends AsyncTask<String, NytimesArticleData, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                // construct url and get result from connection
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                // read result and build a string buffer
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                // parse result string to JSON object
                JSONObject jObject = new JSONObject(result);

//                String status = jObject.getString("status");
//                Log.i("NYTIMES", "status=[" + status + "]");
//
//                String copyright = jObject.getString("copyright");
//                Log.i("NYTIMES", "copyright=[" + copyright + "]");

                // determine how many docs returned
                JSONObject response = jObject.getJSONObject("response");
                JSONArray arrayDocs = response.getJSONArray("docs");
                Log.i("NYTIMES", "Found [" + arrayDocs.length() + "] docs in response");

                // loop every document
                for (int i = 0; i < arrayDocs.length(); ++i) {

                    Log.i("NYTIMES", "doc[" + i + "]");
                    JSONObject doc = arrayDocs.getJSONObject(i);
                    String webUrl = doc.getString("web_url");
                    Log.i("NYTIMES", "\tWeb_url=[" + webUrl + "]");

                    String pubDate = doc.getString("pub_date");
                    Log.i("NYTIMES", "\tpubDate=[" + pubDate + "]");

                    String id = doc.getString("_id");
                    Log.i("NYTIMES", "\tid=[" + id + "]");

                    String title = doc.getJSONObject("headline").getString("main");
                    Log.i("NYTIMES", "\ttitle=[" + title + "]");

                    String strByline = doc.getJSONObject("byline").getString("original");
                    Log.i("NYTIMES", "\tstrByline=[" + strByline + "]");

                    // construct a article object
                    NytimesArticleData article = new NytimesArticleData(id, pubDate, title, strByline, webUrl);

                    // send the article object to UI thread
                    publishProgress(article);
                }
            }
            catch (Exception ex) {
                Log.e("NYTIMES", ex.getMessage());
            }
            return "Finished Task";
        }

        @Override
        protected void onPostExecute(String s) {
            // query finished, let progress bar disappear
            progressBar.setVisibility(View.INVISIBLE);
        }

        /**
         * Runs on the UI thread after {@link #publishProgress} is invoked.
         * The specified values are the values passed to {@link #publishProgress}.
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        @Override
        protected void onProgressUpdate(NytimesArticleData... values) {
            // call adapter to add article object
            adapter.add(values[0]);
        }
    }

    /**
     * class representing an article from New York Times
     * @author leon
     * @since Mar-31-2019
     */
    private class NytimesArticleData {
        /**
         * the id of the article
         */
        private String article_id;

        /**
         * the publish date of the article
         */
        private String article_pubData;

        /**
         * the title of the article
         */
        private String article_title;

        /**
         * the byline of the article
         */
        private String article_byline;

        /**
         * the url of the article
         */
        private String article_url;

        /**
         * constructor
         * @param article_id id
         * @param article_pubData published date
         * @param article_title title
         * @param article_byline byline
         * @param article_url url
         */
        public NytimesArticleData(String article_id, String article_pubData, String article_title, String article_byline, String article_url) {
            this.article_id = article_id;
            this.article_pubData = article_pubData;
            this.article_title = article_title;
            this.article_byline = article_byline;
            this.article_url = article_url;
        }

        /**
         * setter for article id
         * @param article_id original article id in nytimes source
         * @return
         */
        public NytimesArticleData setArticle_id(String article_id) {
            this.article_id = article_id;
            return this;
        }

        /**
         * setter for article publish date
         * @param article_pubData the date published
         * @return
         */
        public NytimesArticleData setArticle_pubData(String article_pubData) {
            this.article_pubData = article_pubData;
            return this;
        }

        /**
         * seeter for article title
         * @param article_title title of the article
         * @return
         */
        public NytimesArticleData setArticle_title(String article_title) {
            this.article_title = article_title;
            return this;
        }

        /**
         * setter for article byline
         * @param article_byline author
         * @return
         */
        public NytimesArticleData setArticle_byline(String article_byline) {
            this.article_byline = article_byline;
            return this;
        }

        /**
         * setter for web url of the article
         * @param article_url web url
         * @return
         */
        public NytimesArticleData setArticle_url(String article_url) {
            this.article_url = article_url;
            return this;
        }

        public String getArticle_id() {
            return article_id;
        }

        public String getArticle_pubData() {
            return article_pubData;
        }

        public String getArticle_title() {
            return article_title;
        }

        public String getArticle_byline() {
            return article_byline;
        }

        public String getArticle_url() {
            return article_url;
        }
    }

    /**
     * Customized Array Adapter for ListView, with built-in container for NytimesArticleData
     */
    private class MyListAdapter extends ArrayAdapter<NytimesArticleData> {
        private LayoutInflater inflater;


        /**
         * Constructor
         * @param context context of parent
         * @param resource resource of the listview
         */
        MyListAdapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // get article
            NytimesArticleData article = getItem(position);

            // inflate custom view
            View view = inflater.inflate(R.layout.layout_nytimes_article_item, null);

            // set pub date
            TextView textView = view.findViewById(R.id.ny_article_pub_date);
            String pubDate = article.getArticle_pubData();
            String date = pubDate.substring(0, pubDate.indexOf("T"));
            textView.setText(date);

            // set title
            textView = view.findViewById(R.id.ny_article_title);
            textView.setText(article.getArticle_title());
            textView = view.findViewById(R.id.ny_article_author);
            textView.setText(article.getArticle_byline());

            // set save button click onclick listener
            Button btnSave = view.findViewById(R.id.ny_article_save);
            btnSave.setOnClickListener(v -> {
                saveArticle(v, article);
            });
            return view;
        }
    }
}