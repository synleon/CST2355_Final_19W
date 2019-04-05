package com.example.cst2355_final_19w;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/** This class is used for connecting the the web "http://webhose.io"  and get useful information
 *  then set the info into a layout file */

public class Activity_nf_url_connector extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText searchEditText;
    private ImageView icon;
    private TextView articleTitle;
    private TextView articleUrl;
    private TextView articleText;
    private NewsAdapter adapter;
    private int positionClicked = 0;
    private Toolbar tBar;

    private Bitmap bitmap;
    private InputStream inputStream;

    public static final String ITEM_SELECTED = "TITLE";
    public static final String ITEM_TEXT = "TEXT";
    public static final String ITEM_URL = "URL";
    public static final String ITEM_POSITION = "POSITION";
    public static  final String ITEM_PIC = "PICTURE";
    public static Drawable drawable;
    private String imageLink;

    protected static ArrayList<NF_Article> NEWS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nf_list_all);

        /** create an object of tool bar and display it.*/
        tBar = (Toolbar) findViewById(R.id.toolbar_search_list);
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

        NFQuery nfQuery = new NFQuery();
        nfQuery.execute("http://webhose.io/filterWebContent?token=264a10ac-9a70-4d5b-9f57-280bb2ec5604&format=xml&sort=crawled&q=" + Activity_nf_main.SEARCHTERM);


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
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putParcelable(ITEM_PIC, NEWS.get(position).getBitmap());

            if (isTablet) {
                NF_Search_DetailFragment dFragment = new NF_Search_DetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, dFragment) //Add the fragment in FrameLayout
                        /*.addToBackStack(null)*/ //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(Activity_nf_url_connector.this, Activity_nf_search_empty.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity);
            } // end of if else
        });
    }

    private class NFQuery extends AsyncTask<String, Integer, String> {
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
                /*if (NEWS.size() >= 3)
                    break;*/

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
            boolean foundImage = false;

            while (true)
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
                } else if (!foundImage && tagName.equals("main_image")) {
                    foundImage = true;
                    imageLink = xpp.nextText();
                    downloadImage(imageLink);
                    Log.e("News Feed ", "Find the image link: " + imageLink);
                    publishProgress(100);
                }
            }

            if (foundTitle && foundText && foundURL && foundImage
                    && urlAddress != null && title != null && text != null && imageLink != null
                    && urlAddress.length() > 0 && title.length() > 0 && text.length() > 0 && imageLink.length() != 0)
                NEWS.add(new NF_Article(title, text, urlAddress, imageLink, bitmap));

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

        protected void downloadImage(String imageLink)
        {
            if (imageLink == null || imageLink.length() == 0)
                return;

            bitmap = null;
            try {
                URL imageUrl = new URL(imageLink);
                HttpURLConnection iconConnecter = (HttpURLConnection) imageUrl.openConnection();
                iconConnecter.setReadTimeout(10000  /* milliseconds */);
                iconConnecter.setConnectTimeout(15000  /* milliseconds */);
                iconConnecter.setDoInput(true);
                iconConnecter.connect();

                // create an object of InputStream
                inputStream = iconConnecter.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);
                compressImage();

                iconConnecter.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * reference _Anonymous_  CSDN https://blog.csdn.net/wudongjiang333/article/details/78122234
         */
        protected void compressImage()
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
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
            return NEWS.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.activity_nf_single_row_type, parent, false);

            NF_Article currentArticle = (NF_Article)getItem(position);

            TextView titleTextView = (TextView) newView.findViewById(R.id.article_title);
            titleTextView.setText(currentArticle.getTitle().toString());

            ImageView image = (ImageView) newView.findViewById(R.id.icon);
            image.setImageBitmap(currentArticle.getBitmap());

            return newView;
        }
    }
}