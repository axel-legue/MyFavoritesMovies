package com.legue.axel.myfavoritesmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.legue.axel.myfavoritesmovies.database.dao.MovieDao;
import com.legue.axel.myfavoritesmovies.database.model.Movie;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class MyFavoritesMoviesDatabase extends RoomDatabase {

    private static final String TAG = MyFavoritesMoviesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();

    private static final String DATABASE_NAME = "MyFavoritesMoviesDatabase";
    private static MyFavoritesMoviesDatabase sInstance;

    public static MyFavoritesMoviesDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creation of Singleton Database");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        MyFavoritesMoviesDatabase.class,
                        MyFavoritesMoviesDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.i(TAG, "Get Database instance: ");
        return sInstance;
    }

    public abstract MovieDao movieDao();

}
