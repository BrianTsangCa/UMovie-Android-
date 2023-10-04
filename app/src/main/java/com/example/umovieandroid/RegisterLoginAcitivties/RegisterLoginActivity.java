package com.example.umovieandroid.RegisterLoginAcitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.umovieandroid.R;

public class RegisterLoginActivity extends AppCompatActivity {
    Button btn_Register, btn_Login;
    final String TAG = "umovie";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        btn_Register = findViewById(R.id.btn_Register);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterLoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterLoginActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}