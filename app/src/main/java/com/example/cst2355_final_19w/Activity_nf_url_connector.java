package com.example.cst2355_final_19w;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Activity_nf_url_connector extends AppCompatActivity
{
    ProgressBar progressBar;
    EditText searchEditText;
    ImageView icon;
    TextView articleTitle;
    TextView articleUrl;
    TextView articleText;
    NewsAdapter adapter;

    protected static ArrayList<NFArticle> NEWS = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_list_all);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        NFQuery nfQuery = new NFQuery();
        nfQuery.execute("http://webhose.io/filterWebContent?" +
                "token=264a10ac-9a70-4d5b-9f57-280bb2ec5604&format=xml&sort=crawled" + Activity_nf_main.SEARCHTERM);

        ListView newsList = (ListView) findViewById(R.id.list_newsF);
        adapter = new NewsAdapter();
        newsList.setAdapter(adapter);

        /** create an object of listView
         *  then use it to call the function setAdapter() with a parameter "adapter" which is an object of
         *  the inner class called NewsAdapter. */

        /*newsList.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent nextActivity = new Intent(Activity_nf_url_connector.this, Activity_listDetail_newsfeed.class );
            startActivity(nextActivity);
        });*/
    }

     private class NFQuery extends AsyncTask<String, Integer, String>
     {
         private final int maxNewsCount = 2;
         private String urlAddress;
         private String title;
         private String text;

         @Override
         protected String doInBackground(String... strings)
         {
             try {
                 parseXMLContent();
             } catch (Exception e) {
                 Log.e("Crash", e.getMessage());
             }
             return "Finish all tasks";
         }

         private void parseXMLContent()
                 throws MalformedURLException, IOException, XmlPullParserException, InterruptedException
         {
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

             /** loop through the xml file*/
             while (xpp.getEventType() != XmlPullParser.END_DOCUMENT)
             {
                 if (xpp.getEventType() == XmlPullParser.START_TAG
                         && xpp.getName().equalsIgnoreCase("post"))
                 {
                     parsePost(xpp);
                     if (NEWS.size() >= maxNewsCount)
                         break;
                 }
                 xpp.next();
             }
         }

         private void parsePost(XmlPullParser xpp)
                 throws IOException, XmlPullParserException, InterruptedException
         {
             // if there are more than one URL, Title, and Text in this post,
             // only fetch the first
             boolean foundURL = false;
             boolean foundTitle = false;
             boolean foundText = false;

             int i=0;
             while (NEWS.size()<=maxNewsCount)
             {
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
                     Thread.sleep(2000);
                 } else if (!foundTitle && tagName.equals("title")){
                     foundTitle = true;
                     title = xpp.nextText();
                     Log.e("News Feed ", "Find a title: " + title);
                     publishProgress(50);
                     Thread.sleep(2000);
                 } else if (!foundText && tagName.equals("text")) {
                     foundText = true;
                     text = xpp.nextText();
                     Log.e("News Feed ", "Find the text: " + text);
                     publishProgress(75);
                     Thread.sleep(2000);

                 }
//                 i++;
             }

             if (foundTitle && foundText && foundURL) {
                 NEWS.add(new NFArticle(title, text, urlAddress));
//                 adapter.notifyDataSetChanged();
             }
         }

         @Override
         protected void onProgressUpdate(Integer... values)
         {

             progressBar.setVisibility(View.VISIBLE);
             progressBar.setProgress(values[0]);
         }

         @Override
         protected void onPostExecute(String s)
         {
          /*   articleTitle = (TextView) findViewById(R.id.titleOfArticle);
             articleTitle.setText(title);

             articleUrl = (TextView) findViewById(R.id.urlOfArticle);
             articleUrl.setText(urlAddress);

             articleText = (TextView) findViewById(R.id.textOfArticle);
             articleText.setText(text);*/
          adapter.notifyDataSetChanged();

             progressBar.setVisibility(View.INVISIBLE);
         }
     }

    protected class NewsAdapter extends BaseAdapter
    {
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
        public View getView(int position, View oldView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.activity_nf_rowlist, parent,false);

            TextView rowText = (TextView) newView.findViewById(R.id.article_title);
            //String textToShow = getItem(position).toString();
            //rowText.setText(textToShow);
            rowText.setText(getItem(position).toString());

            return newView;
        }
    }
}
