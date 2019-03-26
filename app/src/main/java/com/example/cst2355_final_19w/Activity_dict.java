package com.example.cst2355_final_19w;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Activity_dict extends AppCompatActivity {

    /**
     * Array adapter
     */
    private WordListAdapter wordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        Button btn = findViewById(R.id.dict_button);
        btn.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(btn, "Go back?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> finish());
            sb.show();
        });

        Toast.makeText(this, "Welcome to dictionary page!", Toast.LENGTH_LONG).show();

        ProgressBar progressBar = findViewById(R.id.dict_progressBar);

        ListView listViewWordList = findViewById(R.id.dict_list);

        wordListAdapter = new WordListAdapter(this, R.id.dict_list);

        listViewWordList.setAdapter(wordListAdapter);

        wordListAdapter.add("paraphrase");

        wordListAdapter.add("ostensible");

        wordListAdapter.add("digress");

        wordListAdapter.add("uncanny");

        progressBar.setProgress(100);

        // Setting the item click listener for the listview
        listViewWordList.setOnItemClickListener((parent, container, position, id) -> {
            String word = (String)parent.getItemAtPosition(position);
            WordInfoDialog(word);
        });
    }

    /**
     * Display word detail information
     * @param word selected word from the listview
     */
    public void WordInfoDialog(String word) {
        View detailView = getLayoutInflater().inflate(R.layout.layout_word_detail_info, null);
        TextView textViewWord = detailView.findViewById(R.id.textView_word);
        TextView textViewDetail = detailView.findViewById(R.id.textView_word_detail);

        textViewWord.setText(word);

        if (word.equals("paraphrase")) {
            textViewDetail.setText("1 : a restatement of a text, passage, or work giving the meaning in another form\n" +
                    "The teacher asked the students to write a paraphrase of the poem.\n" +
                    "2 : the use or process of paraphrasing in studying or teaching composition\n" +
                    "paraphrase, which aims rather at recapturing the general impression of a foreign work");
        } else if (word.equals("ostensible")) {
            textViewDetail.setText("1 : intended for display : open to view\n" +
                    "2 : being such in appearance : plausible rather than demonstrably true or real");
        } else if (word.equals("digress")) {
            textViewDetail.setText("intransitive verb\n" +
                    "\n" +
                    ": to turn aside especially from the main subject of attention or course of argument");
        } else if (word.equals("uncanny")) {
            textViewDetail.setText("1a : seeming to have a supernatural character or origin : EERIE, MYSTERIOUS\n" +
                    "b : being beyond what is normal or expected : suggesting superhuman or supernatural powers\n" +
                    "an uncanny sense of direction\n" +
                    "2 chiefly Scotland : SEVERE, PUNISHING");
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Word Information").setView(detailView)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }


    /**
     *  A customized array adapter of word
     */
    private class WordListAdapter extends ArrayAdapter<String> {

        /**
         * Inflater used to inflate a layout from the resource
         */
        private LayoutInflater inflater;

        /**
         * constructor
         * @param context
         * @param resource the resource id that identifies the activity
         */
        WordListAdapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        /**
         * Override getView method, used to provide for each item in listview
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
    }
}
