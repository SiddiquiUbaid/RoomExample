package com.appsomniacs.roomexample;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "recentWords")
public class RecentWordsDictionary implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }




    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "meaning")
    private String meaning;



    @Ignore
    public RecentWordsDictionary(int id, String word, String meaning) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
    }




    @Ignore
    public RecentWordsDictionary(String word, String meaning) {

        this.word = word;
        this.meaning = meaning;
    }



    public RecentWordsDictionary(String word) {

        this.word = word;

    }


}
