package com.example.umovieandroid.Vector.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserVector")
public class UserVector {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "userEmail")
    private String UserEmail;
    @ColumnInfo(name = "userVector")
    private String UserVector;

    public UserVector() {
    }

    public UserVector(@NonNull String userEmail, String userVector) {
        UserEmail = userEmail;
        UserVector = userVector;
    }

    @NonNull
    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(@NonNull String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserVector() {
        return UserVector;
    }

    public void setUserVector(String userVector) {
        UserVector = userVector;
    }
}
