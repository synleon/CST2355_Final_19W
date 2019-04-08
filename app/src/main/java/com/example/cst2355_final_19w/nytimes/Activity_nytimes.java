package com.example.cst2355_final_19w.nytimes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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
import android.support.v7.widget.SearchView;
import android.widget.Switch;
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
public class Activity_nytimes extends AppCompatActivity implements SearchView.OnQueryTextListener {

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
     * create the number of CONTAINER_ACTIVITY_REQUESTCODE
     */
    public static final int CONTAINER_ACTIVITY_REQUESTCODE = 135;

    /**
     * The key name of shared preference for last search keyword
     */
    public final String KEY_LASTSEARCH = "lastSearch";

    /**
     * the adapter for articles list view
     */
    private MyListAdapter adapter;

    /**
     * the progress bar
     */
    private ProgressBar progressBar;

    /**
     * last search keyword
     */
    private String lastSearch;

    /**
     * Shared Preferences
     */
    SharedPreferences prefs;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ny_main_toolbar, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor =  prefs.edit();
        // save last search keyword to shared preference file
        editor.putString(KEY_LASTSEARCH, lastSearch);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nytimes);

        // make toast message
        Toast.makeText(this, getString(R.string.nytimes_welcom_message), Toast.LENGTH_LONG).show();

        // initialize customized adapter
        adapter = new MyListAdapter(this, R.id.ny_list);

        ListView theList = findViewById(R.id.ny_list);

        // set adapter to listview
        theList.setAdapter(adapter);

        // initialize progressbar
//        progressBar = findViewById(R.id.ny_progressBar);

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

            // Data to send
            Bundle dataToSend = new Bundle();
            dataToSend.putInt("SAVED", article.getSaved());
            dataToSend.putString("URL", article.getArticle_url());
            dataToSend.putInt("POSITION", position);
            dataToSend.putString("ARTICLE_ID", article.getArticle_id());

            // start an external web browser intent to view the article
            Intent nextActivity = new Intent(Activity_nytimes.this, EmptyContainerActivity.class);
            nextActivity.putExtras(dataToSend);
            startActivityForResult(nextActivity, CONTAINER_ACTIVITY_REQUESTCODE);
        });

        fillListWithSavedArticles();

        // set return button onclick listener
        Button btn = findViewById(R.id.ny_goback_button);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, getString(R.string.goback), Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });

        SearchView searchView = findViewById(R.id.search_nytimes);
        searchView.setOnQueryTextListener(this);

        // read last search keyword and set it to search view
        prefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        lastSearch = prefs.getString(KEY_LASTSEARCH, null);

        if (lastSearch != null && !lastSearch.isEmpty()) {
            // set lastSearch to SearchView
            searchView.setQuery(lastSearch, false);
            // clear focus to avoiding keyboard show
            searchView.clearFocus();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query == null || query.isEmpty()) {
            return false;
        } else {
            // save search keyword
            lastSearch = query;

            // start async task to perform the article search on new york times
            // form full query request url
            String queryString = nytimesUrl + "?api-key=" + nytimesAPIKEY + "&q=" + query.replace(' ', '+');

            // add log info
            Log.i("NYTIMES", "QUERY URL=[" + queryString + "]");

            // let go the query task
            NytimesArticleQuery articleQuery = new NytimesArticleQuery();

            // let progress bar appear
            progressBar = findViewById(R.id.ny_progressBar);
            progressBar.setVisibility(View.VISIBLE);

            articleQuery.execute(queryString);
            return true;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CONTAINER_ACTIVITY_REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                int actionCode = data.getIntExtra("ACTION", 0);
                int position = data.getIntExtra("POSITION", 0);

                ListView listView = findViewById(R.id.ny_list);

                View view = null;

                final int firstListItemPosition = listView.getFirstVisiblePosition();
                final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

                if (position < firstListItemPosition || position > lastListItemPosition ) {
                    view = listView.getAdapter().getView(position, null, listView);
                } else {
                    final int childIndex = position - firstListItemPosition;
                    view = listView.getChildAt(childIndex);
                }

                Switch switchSave = view.findViewById(R.id.ny_article_switch_saved);
                if (actionCode == 1)
                {
                    // save
                    switchSave.setChecked(true);
                    saveArticle(view, true, adapter.getItem(position));
                }
                else if (actionCode == 0) {
                    switchSave.setChecked(false);
                    saveArticle(view, false, adapter.getItem(position));
                }
            }
        }
    }

    /**
     * get saved articles from database and populate the listView
     */
    public void fillListWithSavedArticles() {
        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID,MyDatabaseOpenHelper.COL_PUBDATE,MyDatabaseOpenHelper.COL_AUTHOR,
                MyDatabaseOpenHelper.COL_TITLE,MyDatabaseOpenHelper.COL_URL,MyDatabaseOpenHelper.COL_SAVED};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns,
                null, null, null, null, null, null);

        //iterate over the results, and populate the list view
        //find the column indices:
        int idIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int idSaved = results.getColumnIndex(MyDatabaseOpenHelper.COL_SAVED);
        int idUrl = results.getColumnIndex(MyDatabaseOpenHelper.COL_URL);
        int idTitle = results.getColumnIndex(MyDatabaseOpenHelper.COL_TITLE);
        int idAuthor = results.getColumnIndex(MyDatabaseOpenHelper.COL_AUTHOR);
        int idPubDate = results.getColumnIndex(MyDatabaseOpenHelper.COL_PUBDATE);


        results.moveToFirst();
        while (!results.isAfterLast())
        {
            NytimesArticleData article = new NytimesArticleData(results.getString(idIndex), results.getString(idPubDate), results.getString(idTitle),
                    results.getString(idAuthor), results.getString(idUrl), results.getInt(idSaved));
            adapter.add(article);
            results.moveToNext();
        }
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
     * @param view the view that user clicked save switch
     * @param isSaveChecked whether save switch is checked or not
     * @param article article needs to be saved
     */
    public void saveArticle(View view, boolean isSaveChecked, NytimesArticleData article) {
        Switch switchSave = view.findViewById(R.id.ny_article_switch_saved);
        if (isSaveChecked) {
            if (!isArticleSaved(article)) {
                // save article
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyDatabaseOpenHelper.COL_ID, article.getArticle_id());
                newRowValues.put(MyDatabaseOpenHelper.COL_PUBDATE, article.getArticle_pubData());
                newRowValues.put(MyDatabaseOpenHelper.COL_TITLE, article.getArticle_title());
                newRowValues.put(MyDatabaseOpenHelper.COL_AUTHOR, article.getArticle_byline());
                newRowValues.put(MyDatabaseOpenHelper.COL_URL, article.getArticle_url());
                newRowValues.put(MyDatabaseOpenHelper.COL_SAVED, 1);
                long id = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                String message = (id != -1) ? getString(R.string.nytimes_article_save_success_message) : getString(R.string.nytimes_article_save_fail_message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                switchSave.setText(getString(R.string.nytimes_article_saved));
                article.setSaved(1);
            }
        }
        else {
            // delete article
            String whereClause = MyDatabaseOpenHelper.COL_ID + "=?";
            int rows = db.delete(MyDatabaseOpenHelper.TABLE_NAME, whereClause, new String[]{article.getArticle_id()});
            if (rows > 0) {
                Toast.makeText(this, getString(R.string.nytimes_article_delete_message), Toast.LENGTH_LONG).show();
            }
            switchSave.setText(getString(R.string.nytimes_article_not_saved));
            article.setSaved(0);
        }

    }

    /**
     * check whether an article has been saved based on article
     * @param article article to check
     * @return
     */
    public boolean isArticleSaved(NytimesArticleData article) {
        // construct a query
        String[] colums = {MyDatabaseOpenHelper.COL_ID};
        String whereClause = MyDatabaseOpenHelper.COL_ID + "=?";
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, colums,
                whereClause, new String[]{article.getArticle_id()}, null, null, null, null);
        int nRows = results.getCount();
        if (nRows == 0) {
            return false;
        }
        else {
            return true;
        }
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
                    NytimesArticleData article = new NytimesArticleData(id, pubDate, title, strByline, webUrl, 0);

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
            if (values.length != 0) {
                NytimesArticleData article = values[0];
                // check whether this article has been saved
                if (isArticleSaved(values[0])) {
                    article.setSaved(1);
                }
                else {
                    article.setSaved(0);
                }
                // call adapter to add article object
                adapter.add(values[0]);
            }

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
         * whether has been saved, 0 - not 1 - yes
         */
        private int saved;

        /**
         * constructor
         * @param article_id id
         * @param article_pubData published date
         * @param article_title title
         * @param article_byline byline
         * @param article_url url
         * @param article_saved saved flag
         */
        public NytimesArticleData(String article_id, String article_pubData, String article_title, String article_byline, String article_url, int article_saved) {
            this.article_id = article_id;
            this.article_pubData = article_pubData;
            this.article_title = article_title;
            this.article_byline = article_byline;
            this.article_url = article_url;
            this.saved = article_saved;
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

        /**
         * setter for saved
         * @param saved whether has been saved
         * @return
         */
        public NytimesArticleData setSaved(int saved) {
            this.saved = saved;
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

        public int getSaved() {
            return saved;
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

            // set switch onclick listener
            Switch switchSaved = view.findViewById(R.id.ny_article_switch_saved);

            // set saved switch
            if (article.getSaved() == 1) {
                switchSaved.setChecked(true);
                switchSaved.setText(getString(R.string.nytimes_article_saved));
            }
            else {
                switchSaved.setChecked(false);
                switchSaved.setText(getString(R.string.nytimes_article_not_saved));
            }

            // set listener, has to go after setCheck
            // do not let user to directly click switch button to save/delete article
//            switchSaved.setOnCheckedChangeListener((v, c) -> {
//                saveArticle(v, c, article);
//            });
            switchSaved.setEnabled(false);

            return view;
        }
    }
}