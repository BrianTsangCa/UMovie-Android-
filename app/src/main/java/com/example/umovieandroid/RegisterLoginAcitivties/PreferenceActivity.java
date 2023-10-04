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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferenceActivity extends AppCompatActivity {
    Button btn_save;
    final String TAG = "umovie";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ChipGroup chipGroupGenre, chipGroupEra, chipGroupRating, chipGroupActor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        String status = getIntent().getStringExtra("status");

        int[] chipGroupIds = {R.id.chipGroupGenre, R.id.chipGroupEra, R.id.chipGroupRating, R.id.chipGroupActor};
        chipGroupGenre = findViewById(R.id.chipGroupGenre);
        chipGroupEra = findViewById(R.id.chipGroupEra);
        chipGroupRating = findViewById(R.id.chipGroupRating);
        chipGroupActor = findViewById(R.id.chipGroupActor);
        List<String> checkedChipGroupGenre = new ArrayList<>();
        List<String> checkedChipGroupEra = new ArrayList<>();
        List<String> checkedChipGroupRating = new ArrayList<>();
        List<String> checkedChipGroupActor = new ArrayList<>();

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



        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == null) {
                    startActivity(new Intent(PreferenceActivity.this, MainActivity.class));
                } else {
                    CollectionReference preferenceCollection = db.collection("preferences");
                    Map<String, Object> genrePreferences = new HashMap<>();
                    genrePreferences.put("genre", checkedChipGroupGenre);
                    preferenceCollection.add(genrePreferences);

                    Map<String, Object> eraPreferences = new HashMap<>();
                    eraPreferences.put("era", checkedChipGroupEra);
                    preferenceCollection.add(eraPreferences);
                    Map<String, Object> ratingPreferences = new HashMap<>();
                    ratingPreferences.put("rating", checkedChipGroupRating);
                    preferenceCollection.add(ratingPreferences);
                    Map<String, Object> actorPreferences = new HashMap<>();
                    actorPreferences.put("actor", checkedChipGroupActor);
                    preferenceCollection.add(actorPreferences);

                    preferenceCollection
                            .add(genrePreferences)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    preferenceCollection
                            .add(eraPreferences)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    preferenceCollection
                            .add(ratingPreferences)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    preferenceCollection
                            .add(actorPreferences)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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