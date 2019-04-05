package com.example.cst2355_final_19w.nytimes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.cst2355_final_19w.R;


public class Fragment_nytimes_article extends Fragment {
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


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_nytimes_article_webview, container, false);

        WebView webView = view.findViewById(R.id.nytimes_articleview_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        // set action button text according to saved
        button = view.findViewById(R.id.nytimes_articleview_actionbutton);
        if (saved == 0) {
            button.setText("Save article");
        }
        else if (saved == 1) {
            button.setText("Delete article");
        }

        button.setOnClickListener(v -> {
            EmptyContainerActivity containerActivity = (EmptyContainerActivity)getActivity();
            Intent backToFragment = new Intent();

            //Button buttonSave = view.findViewById(R.id.nytimes_articleview_actionbutton);
            String btnText = button.getText().toString();
            if (btnText.equals("Save article")) {
                backToFragment.putExtra("ACTION", 1);
            }
            else if (btnText.equals("Delete article")) {
                backToFragment.putExtra("ACTION", 0);
            }
            backToFragment.putExtra("POSITION", position);

            containerActivity.setResult(Activity.RESULT_OK, backToFragment);
            containerActivity.finish();
        });

        return view;
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
}

