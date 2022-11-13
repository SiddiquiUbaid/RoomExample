package com.appsomniacs.roomexample;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = RecentWordsDictionary.class, exportSchema = false, version = 1)
public abstract class RecentWordsDictionaryDatabase extends RoomDatabase {


    public static final String DATABASE_NAME = "recentwords_dictionary_db";
    public static final int DATABASE_VERSION = 1;
    private static RecentWordsDictionaryDatabase instance;

    public static  synchronized RecentWordsDictionaryDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), RecentWordsDictionaryDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract RecentWordsDictionaryDao recentWordsDictionaryDao();


}
