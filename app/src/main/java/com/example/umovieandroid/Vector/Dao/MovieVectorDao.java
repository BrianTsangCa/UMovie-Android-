package com.example.umovieandroid.Vector.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.umovieandroid.Vector.Model.MovieVector;

@Dao
public interface MovieVectorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovieVector(MovieVector movieVector) ;

}
