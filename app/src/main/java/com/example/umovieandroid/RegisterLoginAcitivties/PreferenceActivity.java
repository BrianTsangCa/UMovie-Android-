package com.example.umovieandroid.RegisterLoginAcitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.umovieandroid.MainActivity;
import com.example.umovieandroid.R;

public class PreferenceActivity extends AppCompatActivity {
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        String status = getIntent().getStringExtra("status");

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==null){
                    startActivity(new Intent(PreferenceActivity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(PreferenceActivity.this, MainActivity.class));
                }
            }
        });
    }
}