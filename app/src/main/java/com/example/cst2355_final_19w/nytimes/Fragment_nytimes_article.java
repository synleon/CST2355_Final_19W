package com.example.cst2355_final_19w.nytimes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.cst2355_final_19w.R;

import java.io.File;


public class Fragment_nytimes_article extends Fragment implements View.OnClickListener {
    /**
     * create to check is tablet or not
     */
    private boolean isTablet;
    /**
     * create dataFromActivity instance
     */
    private Bundle dataFromActivity;

    /**
     * create position instance
     */
    private int position;

    private Button button;

    private WebView webView;

    /**
     * a string uniquely identified an article of New York Times
     */
    private String article_id;


    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        int saved = dataFromActivity.getInt("SAVED");
        String url = dataFromActivity.getString("URL", "www.google.ca");
        position = dataFromActivity.getInt("POSITION");
        article_id = dataFromActivity.getString("ARTICLE_ID");


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_nytimes_article_webview, container, false);

        webView = view.findViewById(R.id.nytimes_articleview_webview);

        MyWebClient client = new MyWebClient();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(client);

        // if article already exists, load from local storage
        String archiveFile = getContext().getFilesDir().getAbsoluteFile() + File.separator + article_id + ".mht";
        if (isNetworkAvailable()) {
            // always load article from internet when there is a internet connection
            webView.loadUrl(url);
        } else if (fileExistance(archiveFile)) {
            // if no internet connection but archive exists, load archive
            webView.loadUrl("file:///" + archiveFile);
        } else {
            // no internet connection and no archive found, diaplay error information
            Toast.makeText(getContext(), getString(R.string.nytimes_no_internet_connection), Toast.LENGTH_LONG).show();
            return view;
        }

        // set action button text according to saved
        button = view.findViewById(R.id.nytimes_articleview_actionbutton);
        if (saved == 0) {
            button.setText("Save article");
        } else if (saved == 1) {
            button.setText("Delete article");
        }

        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        int actionCode = 0;

        String btnText = button.getText().toString();

        String archiveFileName = getContext().getFilesDir().getAbsoluteFile() + File.separator + article_id + ".mht";

        if (btnText.equals("Save article")) {

            actionCode = 1;

            // save article to local storage as mht file
            webView.saveWebArchive(archiveFileName);
        } else if (btnText.equals("Delete article")) {

            actionCode = 0;

            // delete article archive mht file from local storage
            if (fileExistance(archiveFileName)) {
                File file = new File(archiveFileName);
                file.delete();
            }
        }

        if (isTablet) {
            Activity_nytimes parentActivity = (Activity_nytimes)getActivity();
            if (parentActivity != null) {
                parentActivity.processResult(actionCode, position);
                // remove current fragment
                parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
        }
        else {
            EmptyContainerActivity containerActivity = (EmptyContainerActivity) getActivity();
            Intent backToFragment = new Intent();
            backToFragment.putExtra("ACTION", actionCode);
            backToFragment.putExtra("POSITION", position);
            if (containerActivity != null) {
                containerActivity.setResult(Activity.RESULT_OK, backToFragment);
                containerActivity.finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("NYTIMES_FRAGMENT", "Fragment_nytimes_article.onDestroy()");
    }

    @Override
    public void onDetach() {
        Log.i("NYTIMES_FRAGMENT", "Fragment_nytimes_article.onDetach()");
        super.onDetach();
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }


    /**
     * Determine whether file exists in local storage
     *
     * @param filename the filename to search
     * @return true if file already exists false if not exists
     */
    public boolean fileExistance(String filename) {
        // File file = getContext().getFileStreamPath(filename);
        File file = new File(filename);
        return file.exists();
    }

    /**
     * To check whether there is a internet connection
     *
     * @return true if connection status is ok false of not
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

