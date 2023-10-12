package com.example.umovieandroid.LocalDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.umovieandroid.Vector.Dao.MovieVectorDao;
import com.example.umovieandroid.Vector.Dao.UserVectorDao;
import com.example.umovieandroid.Vector.Model.MovieVector;
import com.example.umovieandroid.Vector.Model.UserVector;

@Database(entities = {UserVector.class, MovieVector.class}, version = 1, exportSchema = false)
public abstract class UMovieDatabase extends RoomDatabase {
    public abstract MovieVectorDao movieVectorDao();
    public abstract UserVectorDao userVectorDao();
}
