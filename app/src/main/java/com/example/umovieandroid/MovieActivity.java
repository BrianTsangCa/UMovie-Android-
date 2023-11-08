package com.example.umovieandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umovieandroid.Model.Movie;
import com.example.umovieandroid.Vector.Dao.MovieVectorDao;
import com.example.umovieandroid.Vector.Dao.UserVectorDao;
import com.example.umovieandroid.Vector.Model.UserVector;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieActivity extends AppCompatActivity {
    final String TAG = "umovie";
    TextView txt_title, txt_rating, txt_year, txt_overview;
    ImageView imgView_movie_pic;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference watchListInfo = db.collection("Watch List");
    CollectionReference dislikeListInfo = db.collection("Dislike List");
    String userEmail;
    FirebaseUser user;
    String watchList_movieList = "";
    String dislikeList_movieList = "";
    Chip buttonWatchList, button_block;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        user = FirebaseAuth.getInstance().getCurrentUser();
        txt_title = findViewById(R.id.txt_title);
        txt_rating = findViewById(R.id.txt_rating);
        txt_year = findViewById(R.id.txt_year);
        txt_overview = findViewById(R.id.txt_overview);
        imgView_movie_pic = findViewById(R.id.imgView_movie_pic);
        button_block = findViewById(R.id.buttonWatchList);
        button_block = findViewById(R.id.button_block);
        String overview = getIntent().getStringExtra("overview");
        String backdrop_path = "" + getIntent().getStringExtra("backdrop");
        title = getIntent().getStringExtra("title");
        String rating = getIntent().getStringExtra("rating");
        double vote_count = getIntent().getDoubleExtra("vote_count", 0);
        String release_date = getIntent().getStringExtra("year");
        int score = getIntent().getIntExtra("score", -1);
        txt_title.setText(title);
        txt_rating.setText("Rating: " + rating);
        if (score != -1) {
            txt_year.setText(score + " % Matches " + "Year: " + release_date.substring(0, 4) + " Vote count:" + vote_count);
        } else {
            txt_year.setText("Year: " + release_date.substring(0, 4));
        }
        txt_overview.setText(overview);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + backdrop_path).into(imgView_movie_pic);
        if (user != null) {
            userEmail = user.getEmail();
        }
        getMovieWatch_Dislike_List();

        button_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkWhetherMovieIsAddedDislikeList()) {
                    if (dislikeList_movieList.indexOf("_") == -1) {
                        dislikeList_movieList = "";
                    } else {
                        String[] temp = dislikeList_movieList.split("_");
                        String[] temp2 = new String[temp.length - 1];
                        int index = 0;
                        for (int i = 0; i < temp.length; i++) {
                            if (!temp[i].equals(title)) {
                                temp2[index] = temp[i];
                                index++;
                            }
                        }
                        dislikeList_movieList = String.join("_", temp2);
                    }
                    storeMovieDislikeList();
                    checkOnList();
                } else {
                    String[] temp = dislikeList_movieList.split("_");
                    if (!temp[0].equals("")) {
                        dislikeList_movieList = String.join("_", temp) + "_" + title;
                    } else {
                        dislikeList_movieList = title;
                    }
                    storeMovieDislikeList();
                    checkOnList();
                }
            }
        });
    }

    private void checkOnList() {
        if (checkWhetherMovieIsAddedWatchList()) {
            buttonWatchList.setChecked(true);
        }
        if (checkWhetherMovieIsAddedDislikeList()) {
            button_block.setChecked(true);
        }
    }

    private int searchForTitle(List<Movie> movieList, String title) {
        for (int i = 0; i < movieList.size(); i++) {
            if (movieList.get(i).getTitle().equals("title")) {
                return i;
            }
        }
        return -1;
    }

    private Boolean checkWhetherMovieIsAddedDislikeList() {
        String[] temp = dislikeList_movieList.split("_");
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals(title)) {
                Toast.makeText(this, "Found!" + temp[i], Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        Toast.makeText(this, "Not Found!" + temp[0], Toast.LENGTH_SHORT).show();
        return false;
    }

    private Boolean checkWhetherMovieIsAddedWatchList() {
        String[] temp = watchList_movieList.split("_");
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals(title)) {
                return true;
            }
        }
        return false;
    }

    private void storeMovieWatchList() {
        Map<String, Object> preferenceCollection = new HashMap<>();
        preferenceCollection.put("Watch List", watchList_movieList);
        db.collection("Watch List").document(userEmail)
                .set(preferenceCollection)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot adding with email: " + userEmail);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void storeMovieDislikeList() {
        Map<String, Object> preferenceCollection = new HashMap<>();
        preferenceCollection.put("Dislike List", dislikeList_movieList);
        db.collection("Dislike List").document(userEmail)
                .set(preferenceCollection)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot adding with email: " + userEmail);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void getMovieWatch_Dislike_List() {
        dislikeListInfo.document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> preferences = documentSnapshot.getData();
                    if (preferences != null && preferences.containsKey("Dislike List")) {
                        dislikeList_movieList = (String) preferences.get("Dislike List");
                        checkOnList();
                    }
                }
            }
        });
        watchListInfo.document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> preferences = documentSnapshot.getData();
                    if (preferences != null && preferences.containsKey("Watch List")) {
                        watchList_movieList = (String) preferences.get("Watch List");
                        checkOnList();
                    }
                }
            }
        });

    }
}