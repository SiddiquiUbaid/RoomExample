package com.appsomniacs.roomexample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;
@Dao

public interface DictionaryDao {

    @Query("SELECT * FROM dictionary")
    List<Dictionary> getDictionaryList();

    @Query("SELECT meaning FROM dictionary WHERE word LIKE :searchWord")
    String getMeaning(String searchWord);

    @Query("SELECT meaning FROM dictionary WHERE word LIKE :searchWord")
    List<String> getMeaningsList(String searchWord);

    @Query("SELECT * FROM dictionary WHERE word LIKE :searchWord")
    List<Dictionary> getDictionaryByWord(String searchWord);


    @Insert
    void insertDictionary(Dictionary dictionary);

    @Update
    void updateDictionary(Dictionary dictionary);

    @Delete
    void deleteDictionary(Dictionary dictionary);


}
