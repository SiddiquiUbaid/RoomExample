package com.appsomniacs.roomexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Adapter.MeaningsListAdapter;

public class SearchActivity extends AppCompatActivity implements Serializable, MeaningsListAdapter.OnMeaningsListener {

    LinearLayout linearSearchBar;
    ImageButton btnMicSearch;
    ImageButton btnCloseSearch;
    ImageButton btnSearchIcon;
    EditText etSearchText;
    TextView tvListTitle;
    ImageView iconDeleteRecents;


    RecyclerView meaningsListRecyclerView;
    MeaningsListAdapter meaningsListAdapter;

    DictionaryDatabase appDb;
    RecentWordsDictionaryDatabase recentAppDb;

    String queryString;

    List<Dictionary> meaningsList;

    public SearchActivity() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        linearSearchBar = (LinearLayout) findViewById(R.id.linearSearchBar);
        btnMicSearch = (ImageButton) findViewById(R.id.btnMicSearch);
        btnCloseSearch = (ImageButton) findViewById(R.id.btnCloseSearch);
        btnSearchIcon = (ImageButton) findViewById(R.id.btnSearchIcon);
        etSearchText = (EditText) findViewById(R.id.etSearchText);
        tvListTitle = (TextView) findViewById(R.id.tvListTitle);
        meaningsListRecyclerView = findViewById(R.id.meaningsListRecycler);
        iconDeleteRecents = findViewById(R.id.iconDeleteRecents);


        //starting activity with recent words on recyclerview

        tvListTitle.setText("حالیہ الفاظ");
        SearchActivity.BgThreadForRecentWords threadForSearch = new SearchActivity.BgThreadForRecentWords();
        threadForSearch.start();

        iconDeleteRecents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SearchActivity.BgThreadForDeleteRecents threadForDeleteRecents = new SearchActivity.BgThreadForDeleteRecents();
                threadForDeleteRecents.start();

            }
        });



        btnMicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();

            }
        });

        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                meaningsListAdapter.clear();
                btnCloseSearch.setVisibility(View.GONE);
                etSearchText.setText("");
                tvListTitle.setText("حالیہ الفاظ");

                SearchActivity.BgThreadForRecentWords threadForSearch = new SearchActivity.BgThreadForRecentWords();
                threadForSearch.start();


                //finish();
            }
        });




        etSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passString = ""+charSequence;

                if(passString.equals("")){

                    btnCloseSearch.setVisibility(View.GONE);
                    iconDeleteRecents.setVisibility(View.VISIBLE);

                    meaningsListAdapter.clear();
                    tvListTitle.setText("حالیہ الفاظ");
                    SearchActivity.BgThreadForRecentWords threadForSearch = new SearchActivity.BgThreadForRecentWords();
                    threadForSearch.start();

                }
                else{

                    iconDeleteRecents.setVisibility(View.GONE);

                    meaningsListAdapter.clear();
                    tvListTitle.setText("نتائج");
                    SearchActivity.BgThreadForSearch threadForSearch = new SearchActivity.BgThreadForSearch(passString);
                    threadForSearch.start();

                    btnCloseSearch.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    private void speak() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ur");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "لافز کاھعن");

        try{
            startActivityForResult(intent, 1);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && null!=data){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //Toast.makeText(getApplicationContext(), ""+result.get(0), Toast.LENGTH_SHORT).show();
            etSearchText.setText(""+result.get(0));
        }


    }

    @Override
    public void onMeaningClick(int position) {
        //open details activity
        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
        intent.putExtra("meaningsList", (Serializable)meaningsList.get(position));
        startActivity(intent);



        // add word to recent words
        String getRecentWord = meaningsList.get(position).getWord();
        SearchActivity.ThreadForAddRecentWords threadForRecentWords = new SearchActivity.ThreadForAddRecentWords(getRecentWord);
        threadForRecentWords.start();



    }


    class BgThreadForRecentWords extends Thread {
        String mQuery;

        public  BgThreadForRecentWords(){

        }

        public void run() {

            super.run();

            recentAppDb = RecentWordsDictionaryDatabase.getInstance(getApplicationContext());
            meaningsList = recentAppDb.recentWordsDictionaryDao().getDictionaryForRecentWords();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if(meaningsList.isEmpty()){
                        iconDeleteRecents.setVisibility(View.GONE);
                    }

                    meaningsListAdapter=new MeaningsListAdapter(getApplicationContext(), meaningsList, SearchActivity.this::onMeaningClick);
                    meaningsListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                    meaningsListRecyclerView.setAdapter(meaningsListAdapter);
                    meaningsListAdapter.notifyDataSetChanged();


                }
            });




        }



    }




    class ThreadForAddRecentWords extends Thread {
        String recentWord;
        Boolean isRecentExist;

        ThreadForAddRecentWords(String recentWord){
            this.recentWord = recentWord;
        }

        public void run(){

            super.run();

            RecentWordsDictionaryDatabase recentAppDb = RecentWordsDictionaryDatabase.getInstance(getApplicationContext());
            List<Dictionary> tempDictionary = recentAppDb.recentWordsDictionaryDao().getDictionaryForRecentWords();


            isRecentExist = false;
            for(Dictionary d: tempDictionary){

                if(d.getWord().equals(recentWord)){
                    isRecentExist = true;
                    break;

                }

            }

            if(!isRecentExist){

                //finally add it in the table
                RecentWordsDictionary dictionary = new RecentWordsDictionary(recentWord);
                recentAppDb.recentWordsDictionaryDao().insertDictionary(dictionary);

                //maybe runOnUi used
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();

                    }
                });

            }
            else{
                //maybe runOnUi used
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), "already exist", Toast.LENGTH_SHORT).show();

                    }
                });
            }





        }

    }



    class BgThreadForSearch extends Thread {
        String mQuery;

        public  BgThreadForSearch(String query){
            mQuery = "%" + query.trim() + "%";
        }

        public void run() {

            super.run();

            appDb = DictionaryDatabase.getInstance(getApplicationContext());
            meaningsList = appDb.dictionaryDao().getDictionaryByWord(mQuery);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    meaningsListAdapter=new MeaningsListAdapter(getApplicationContext(), meaningsList, SearchActivity.this::onMeaningClick);
                    meaningsListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                    meaningsListRecyclerView.setAdapter(meaningsListAdapter);
                    meaningsListAdapter.notifyDataSetChanged();


                }
            });







        }



    }



    class BgThreadForDeleteRecents extends Thread {

        public  BgThreadForDeleteRecents(){

        }

        public void run() {

            super.run();

            recentAppDb = RecentWordsDictionaryDatabase.getInstance(getApplicationContext());
            recentAppDb.recentWordsDictionaryDao().deleteRecentWords();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    iconDeleteRecents.setVisibility(View.GONE);
                    meaningsListAdapter.clear();
                    Toast.makeText(getApplicationContext(), "list cleared", Toast.LENGTH_SHORT).show();


                }
            });




        }



    }








}

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//
//
//                }
//            });
