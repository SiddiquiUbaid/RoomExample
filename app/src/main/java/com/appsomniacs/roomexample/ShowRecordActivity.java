package com.appsomniacs.roomexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import Adapter.MeaningsListAdapter;

public class ShowRecordActivity extends AppCompatActivity implements Serializable, MeaningsListAdapter.OnMeaningsListener {

    //EditText etSearchWord;
    SearchView svSearchWord;

    TextView tvShowMeaning;
    Button btnSearchWord;

    RecyclerView meaningsListRecyclerView;
    MeaningsListAdapter meaningsListAdapter;

    DictionaryDatabase appDb;
    String queryString;

    List<Dictionary> meaningsList;

    public ShowRecordActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);

        //etSearchWord = findViewById(R.id.etSearchWord);
        svSearchWord = findViewById(R.id.svSearchWord);
        tvShowMeaning = findViewById(R.id.tvShowMeaning);
        btnSearchWord = findViewById(R.id.btnSearch);
        meaningsListRecyclerView = findViewById(R.id.meaningsListRecycler);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        svSearchWord.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));




        btnSearchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BgThreadForSearch threadForSearch = new BgThreadForSearch();
//                threadForSearch.start();

               // populateMeanings();


            }
        });


        svSearchWord.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String passString) {

                if(passString.equals("")){

                    meaningsListAdapter.clear();

                }
                else{
                    BgThreadForSearch threadForSearch = new BgThreadForSearch(passString);
                    threadForSearch.start();
                }

                return false;
            }
        });








    }


    public void populateMeanings(String queryString){

        class BgThreadForSearch extends Thread {

            public void run() {

                super.run();


                appDb = DictionaryDatabase.getInstance(getApplicationContext());
                meaningsList = appDb.dictionaryDao().getDictionaryByWord(queryString);

                meaningsListAdapter=new MeaningsListAdapter(getApplicationContext(), meaningsList, ShowRecordActivity.this::onMeaningClick);
                meaningsListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                meaningsListRecyclerView.setAdapter(meaningsListAdapter);
                meaningsListAdapter.notifyDataSetChanged();



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

            //queryString = "%" + etSearchWord.getText().toString().trim() + "%";


            meaningsList = appDb.dictionaryDao().getDictionaryByWord(mQuery);


            runOnUiThread(new Runnable() {
            @Override
            public void run() {

                meaningsListAdapter=new MeaningsListAdapter(getApplicationContext(), meaningsList, ShowRecordActivity.this::onMeaningClick);
                meaningsListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                meaningsListRecyclerView.setAdapter(meaningsListAdapter);
                meaningsListAdapter.notifyDataSetChanged();


            }
        });







        }



    }



    @Override
    public void onMeaningClick(int position) {

        Toast.makeText(getApplicationContext(), ""+meaningsList.get(position).getMeaning(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
        intent.putExtra("meaningsList", (Serializable)meaningsList.get(position));

        startActivity(intent);

    }





}