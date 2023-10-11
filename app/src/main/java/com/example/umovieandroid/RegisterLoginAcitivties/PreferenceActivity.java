package com.example.umovieandroid.RegisterLoginAcitivties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.umovieandroid.MainActivity;
import com.example.umovieandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferenceActivity extends AppCompatActivity {
    Button btn_save;
    final String TAG = "umovie";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    List<String> genreList=new ArrayList<>();
    List<String> eraList=new ArrayList<>();
    List<String> ratingList=new ArrayList<>();
    List<String> actorList=new ArrayList<>();

    ChipGroup chipGroupGenre, chipGroupEra, chipGroupRating, chipGroupActor;
    CollectionReference preferencesRef = db.collection("preferences");
    CollectionReference usersRef = db.collection("users");
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        String status = getIntent().getStringExtra("status");
        userEmail = getIntent().getStringExtra("email");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }
        chipGroupGenre = findViewById(R.id.chipGroupGenre);
        chipGroupEra = findViewById(R.id.chipGroupEra);
        chipGroupRating = findViewById(R.id.chipGroupRating);
        chipGroupActor = findViewById(R.id.chipGroupActor);
        List<String> checkedChipGroupGenre = new ArrayList<>();
        List<String> checkedChipGroupEra = new ArrayList<>();
        List<String> checkedChipGroupRating = new ArrayList<>();
        List<String> checkedChipGroupActor = new ArrayList<>();
        btn_save = findViewById(R.id.btn_save);
        if (status.equals("Setting")) {
            preferencesRef.document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> preferences = documentSnapshot.getData();
                        if (preferences != null && preferences.containsKey("genre")) {
                            genreList = (List<String>) preferences.get("genre");
                        }
                        if (preferences != null && preferences.containsKey("era")) {
                            eraList = (List<String>) preferences.get("era");
                        }
                        if (preferences != null && preferences.containsKey("rating")) {
                            ratingList = (List<String>) preferences.get("rating");
                        }
                        if (preferences != null && preferences.containsKey("actor")) {
                            actorList = (List<String>) preferences.get("actor");
                        }
                    }
                    for (int i = 0; i < chipGroupGenre.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupGenre.getChildAt(i);
                        String chipText = chip.getText().toString();
                        if (genreList.contains(chipText)) {
                            chip.setChecked(true);
                        }
                    }
                    for (int i = 0; i < chipGroupEra.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupEra.getChildAt(i);
                        String chipText = chip.getText().toString();
                        if (eraList.contains(chipText)) {
                            chip.setChecked(true);
                        }
                    }
                    for (int i = 0; i < chipGroupRating.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupRating.getChildAt(i);
                        String chipText = chip.getText().toString();
                        if (ratingList.contains(chipText)) {
                            chip.setChecked(true);
                        }
                    }
                    for (int i = 0; i < chipGroupActor.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupActor.getChildAt(i);
                        String chipText = chip.getText().toString();
                        if (actorList.contains(chipText)) {
                            chip.setChecked(true);
                        }
                    }
                }
            });
        }
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == null) {
                    startActivity(new Intent(PreferenceActivity.this, MainActivity.class));
                } else if (status.equals("Registration")) {
                    for (int i = 0; i < chipGroupGenre.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupGenre.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupGenre.add(chip.getText().toString());
                        }
                    }
                    for (int i = 0; i < chipGroupEra.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupEra.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupEra.add(chip.getText().toString());
                        }
                    }
                    for (int i = 0; i < chipGroupRating.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupRating.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupRating.add(chip.getText().toString());
                        }
                    }
                    for (int i = 0; i < chipGroupActor.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupActor.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupActor.add(chip.getText().toString());
                        }
                    }
                    Map<String, Object> preferenceCollection = new HashMap<>();
                    preferenceCollection.put("genre", checkedChipGroupGenre);
                    preferenceCollection.put("era", checkedChipGroupEra);
                    preferenceCollection.put("rating", checkedChipGroupRating);
                    preferenceCollection.put("actor", checkedChipGroupActor);

                    db.collection("preferences").document(userEmail)
                            .update(preferenceCollection)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot updated with email: " + userEmail);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                    startActivity(new Intent(PreferenceActivity.this, MainActivity.class));
                } else if (status.equals("Setting")) {
                    for (int i = 0; i < chipGroupGenre.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupGenre.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupGenre.add(chip.getText().toString());
                        }
                    }
                    for (int i = 0; i < chipGroupEra.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupEra.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupEra.add(chip.getText().toString());
                        }
                    }
                    for (int i = 0; i < chipGroupRating.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupRating.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupRating.add(chip.getText().toString());
                        }
                    }
                    for (int i = 0; i < chipGroupActor.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupActor.getChildAt(i);
                        if (chip.isChecked()) {
                            checkedChipGroupActor.add(chip.getText().toString());
                        }
                    }
                    Map<String, Object> preferenceCollection = new HashMap<>();
                    preferenceCollection.put("genre", checkedChipGroupGenre);
                    preferenceCollection.put("era", checkedChipGroupEra);
                    preferenceCollection.put("rating", checkedChipGroupRating);
                    preferenceCollection.put("actor", checkedChipGroupActor);

                    db.collection("preferences").document(userEmail)
                            .set(preferenceCollection)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot added with email: " + userEmail);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    startActivity(new Intent(PreferenceActivity.this, MainActivity.class));
                }
            }
        });
    }
}