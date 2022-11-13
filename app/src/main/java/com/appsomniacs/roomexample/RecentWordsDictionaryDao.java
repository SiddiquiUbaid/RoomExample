package com.appsomniacs.roomexample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao

public interface RecentWordsDictionaryDao {

    @Query("SELECT * FROM recentWords")
    List<Dictionary> getDictionaryForRecentWords();

    @Query("DELETE FROM recentWords")
    public void deleteRecentWords();



    @Insert
    void insertDictionary(RecentWordsDictionary recentWords);





}
