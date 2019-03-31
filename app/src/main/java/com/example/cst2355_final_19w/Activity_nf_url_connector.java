package com.example.cst2355_final_19w;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/** This class is used for connecting the the web "http://webhose.io"  and get useful information
 *  then set the info into a layout file */

public class Activity_nf_url_connector extends AppCompatActivity
{
    ProgressBar progressBar;
    EditText searchEditText;
    ImageView icon;
    TextView articalTitle;
    TextView url;

    String searchTerm;
    String urlAddress;
    String title;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_mainlayout);

        NFQuery nfQuery = new NFQuery();
        nfQuery.execute("http://webhose.io/filterWebContent?token=264a10ac-9a70-4d5b-9f57-280bb2ec5604&format=xml&sort=crawled" + Activity_nf_main.SEARCHTERM);

        finish();
    }

     private class NFQuery extends AsyncTask<String, Integer, String>
     {

         @Override
         protected String doInBackground(String... strings)
         {
             try {
                 /** connect to an url to find the weather info */
                 URL url = new URL("http://webhose.io/filterWebContent?token=264a10ac-9a70-4d5b-9f57-280bb2ec5604&format=xml&sort=crawled&q=" + Activity_nf_main.SEARCHTERM);
                 HttpURLConnection webhoseConnecter = (HttpURLConnection) url.openConnection();

                 /** get input stream*/
                 InputStream inStream = webhoseConnecter.getInputStream();

                 /** create a pull parser use the Factory pattern*/
                 XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                 factory.setNamespaceAware(false);
                 XmlPullParser xpp = factory.newPullParser();
                 xpp.setInput(inStream, "UTF-8");

                 /** loop through the xml file*/
                 while (xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                 {
                     if (xpp.getEventType() == XmlPullParser.START_TAG)
                     {
                         String tagName = null;
                         tagName = xpp.getName();

                         /*switch (tagName)
                         {
                             case "url":
                                 urlAddress = xpp.getText();
                                 Log.e("News Feed ", "Find URL: " + urlAddress);
                                 publishProgress(25);
                                 Thread.sleep(2000);
                                 break;
                             case "title":
                                 title = xpp.getText();
                                 Log.e("News Feed ", "Find a title: " + title);
                                 publishProgress(50);
                                 Thread.sleep(2000);
                                 break;
                             case "text":
                                 text = xpp.getText();
                                 Log.e("News Feed ", "Find the text: " + text);
                                 publishProgress(75);
                                 Thread.sleep(2000);
                         }*/


                        if(tagName.equals("url"))
                            urlAddress = xpp.getText();
                        else if(tagName.equals("title"))
                            title = xpp.nextText();
                        else if(tagName.equals("text"))
                            text = xpp.getText();
                     }
                     xpp.next();
                 }

             } catch (Exception e) {
                 Log.e("Crash", e.getMessage());
             }
             return "Finish all tasks";
         }

         @Override
         protected void onProgressUpdate(Integer... values)
         {
             progressBar = (ProgressBar) findViewById(R.id.progressBar);
             progressBar.setVisibility(View.VISIBLE);
             progressBar.setProgress(values[0]);
         }

         @Override
         protected void onPostExecute(String s)
         {
             articalTitle = (TextView) findViewById(R.id.article_title);
             articalTitle.setText(title);

             url = (TextView) findViewById(R.id.urlAddress);
             url.setText(urlAddress);
         }
     }
}
