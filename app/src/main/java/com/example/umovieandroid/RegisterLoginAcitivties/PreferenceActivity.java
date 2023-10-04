package com.example.umovieandroid.RegisterLoginAcitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.umovieandroid.R;

public class PreferenceActivity extends AppCompatActivity {
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        btn_save = findViewById(R.id.btn_save);
    }
}