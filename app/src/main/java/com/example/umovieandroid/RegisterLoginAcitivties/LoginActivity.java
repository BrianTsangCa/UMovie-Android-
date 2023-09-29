package com.example.umovieandroid.RegisterLoginAcitivties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.umovieandroid.MainActivity;
import com.example.umovieandroid.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText txtemail_login, txtpassword_login;
    Button elevatedButton_login;
    FirebaseAuth firebaseAuth;
//    FirebaseUser user;
//    BeginSignInRequest signInRequest;
//    ImageButton btnSignInWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        elevatedButton_login = findViewById(R.id.elevatedButton_login);
        txtemail_login = findViewById(R.id.txtemail_login);
        txtpassword_login = findViewById(R.id.txtpassword_login);
        firebaseAuth = FirebaseAuth.getInstance();
        elevatedButton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtemail_login.getText().toString();
                String password = txtpassword_login.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
//        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle);
//        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BeginSignInRequest signInRequest;
//                signInRequest = BeginSignInRequest.builder()
//                        .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                                .setSupported(true)
//                                // Your server's client ID, not your Android client ID.
//                                .setServerClientId(getString(R.string.web_client_id))
//                                // Only show accounts previously used to sign in.
//                                .setFilterByAuthorizedAccounts(true)
//                                .build()).build();
//            }
//        });


    }

//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//    }
}
