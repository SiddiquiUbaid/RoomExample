package com.appsomniacs.roomexample;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Dictionary.class, exportSchema = false, version = 1)
public abstract class DictionaryDatabase extends RoomDatabase {


    public static final String DATABASE_NAME = "dictionary_db";
    public static final int DATABASE_VERSION = 1;
    private static  DictionaryDatabase instance;

    public static  synchronized DictionaryDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DictionaryDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract DictionaryDao dictionaryDao();


}
