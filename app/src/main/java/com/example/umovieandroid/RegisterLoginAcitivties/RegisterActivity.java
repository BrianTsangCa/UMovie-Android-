package com.example.umovieandroid.RegisterLoginAcitivties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.umovieandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    final String TAG = "umovie";
    TextInputLayout txtPassword_reg;
    TextInputEditText txtemail_reg, txtpassword_reg, txtconfirmpassword_reg, txtusername_reg;
    Button elevatedButton_register;
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("username");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        txtPassword_reg = findViewById(R.id.txtPassword_reg);
        txtemail_reg = findViewById(R.id.txtemail_reg);
        txtpassword_reg = findViewById(R.id.txtpassword_reg);
        txtconfirmpassword_reg = findViewById(R.id.txtconfirmpassword_reg);
        txtusername_reg = findViewById(R.id.txtusername_reg);
        elevatedButton_register = findViewById(R.id.elevatedButton_register);

        firebaseAuth = FirebaseAuth.getInstance();
        elevatedButton_register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                String _email = txtemail_reg.getText().toString();
                String _password = txtpassword_reg.getText().toString();
                String _userName = txtusername_reg.getText().toString();
                String _confirmpassword = txtconfirmpassword_reg.getText().toString();
                if (_email.isEmpty()) {
                    txtemail_reg.setError("Please input Email");
                    txtPassword_reg.setHelperTextEnabled(false);
                    return;
                } else if (_password.isEmpty()) {
                    txtpassword_reg.setError("Please input Password");
                    txtPassword_reg.setHelperTextEnabled(false);
                    return;
                } else if (_userName.isEmpty()) {
                    txtusername_reg.setError("Please input Username");
                    txtPassword_reg.setHelperTextEnabled(false);
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
                    txtemail_reg.setError("Invalid Email");
                    txtPassword_reg.setHelperTextEnabled(false);
                    return;
                } else if (!_confirmpassword.equals(_password)) {
                    txtPassword_reg.setHelperTextEnabled(true);
                    txtPassword_reg.setHelperText("Password are not matching");
                    int redColor = ContextCompat.getColor(RegisterActivity.this, R.color.red);
                    txtPassword_reg.setHelperTextColor(ColorStateList.valueOf(redColor));
                    return;
                }
                try {
                    firebaseAuth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User is Created!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, PreferenceActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}