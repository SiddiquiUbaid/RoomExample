package com.appsomniacs.roomexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText tvWord, tvMeaning;
    Button btnAdd, btnShow;
    TextView tvSampleText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvWord = findViewById(R.id.tvWord);
        tvMeaning = findViewById(R.id.tvMeaning);
        btnAdd = findViewById(R.id.btnAdd);
        btnShow = findViewById(R.id.btnShow);
        tvSampleText1 = findViewById(R.id.tvSampleText1);

        String styledText = ""+ "<font  color=\"red\" size=\"3\" >S(x)=a<sub>n</sub>x<sup>n</sup>+a<sub>n-1</sub>x<sup>n-1</sup>+a<sub>n-2</sub>x<sup>n-2</sup>+....+ax+a<sub>0</sub></font>" ;

        tvSampleText1.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);


        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new bgthread().start();


            }



        });


        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent);


//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(intent);

            }
        });
        
        
        
    }




    class bgthread extends Thread {

        public void run(){

            super.run();

            DictionaryDatabase appDb = DictionaryDatabase.getInstance(getApplicationContext());
            Dictionary dictionary = new Dictionary(tvWord.getText().toString(), tvMeaning.getText().toString());
            appDb.dictionaryDao().insertDictionary(dictionary);

            //Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
            tvWord.setText("");
            tvMeaning.setText("");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();

                }
            });









        }

    }


}