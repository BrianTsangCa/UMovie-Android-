package com.example.umovieandroid.Vector.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.umovieandroid.Vector.Model.UserVector;

@Dao
public interface UserVectorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserVector(UserVector userVector) ;

}
