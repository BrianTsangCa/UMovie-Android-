package com.example.umovieandroid.Vector.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MovieVector")
public class MovieVector {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "movieTitle")
    private String MovieTitle;
    @ColumnInfo(name = "movieVector")
    private String MovieVector;

    public MovieVector() {
    }

    public MovieVector(@NonNull String movieTitle, String movieVector) {
        MovieTitle = movieTitle;
        MovieVector = movieVector;
    }

    @NonNull
    public String getMovieTitle() {
        return MovieTitle;
    }

    public void setMovieTitle(@NonNull String movieTitle) {
        MovieTitle = movieTitle;
    }

    public String getMovieVector() {
        return MovieVector;
    }

    public void setMovieVector(String movieVector) {
        MovieVector = movieVector;
    }
}
