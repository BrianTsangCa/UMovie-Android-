package com.example.umovieandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umovieandroid.Adapter.CommentAdapter;
import com.example.umovieandroid.Model.Comment;
import com.example.umovieandroid.Model.Movie;
import com.example.umovieandroid.Model.MovieComment;
import com.example.umovieandroid.Vector.Dao.MovieVectorDao;
import com.example.umovieandroid.Vector.Dao.UserVectorDao;
import com.example.umovieandroid.Vector.Model.UserVector;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

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
    CollectionReference usersInfo = db.collection("users");
    CollectionReference watchListInfo = db.collection("Watch List");
    CollectionReference dislikeListInfo = db.collection("Dislike List");
    CollectionReference commentListInfo = db.collection("Comment List");
    EditText EditText_comment;
    String userEmail;
    String userName;
    FirebaseUser user;
    MovieComment movieCommentList;
    List<Comment> commentList = new ArrayList<>();
    String watchList_movieList = "";
    String dislikeList_movieList = "";
    Chip buttonWatchList, button_block;
    List<String> CommentUser = new ArrayList<>();
    List<String> CommentDetails = new ArrayList<>();
    String title;
    String id;
    RecyclerView recyclerView_comment;
    ExtendedFloatingActionButton fab_submit;

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
        buttonWatchList = findViewById(R.id.buttonWatchList);
        button_block = findViewById(R.id.button_block);
        recyclerView_comment = findViewById(R.id.recyclerView_comment);
        fab_submit = findViewById(R.id.fab_submit);
        EditText_comment = findViewById(R.id.EditText_comment);
        id = getIntent().getStringExtra("id");
        movieCommentList = new MovieComment(Integer.parseInt(id), commentList);
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
        getUserName_Comment();
        getMovieWatch_Dislike_List();
        buttonWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkWhetherMovieIsAddedWatchList()) {
                    if (watchList_movieList.indexOf("_") == -1) {
                        watchList_movieList = "";
                    } else {
                        String[] temp = watchList_movieList.split("_");
                        String[] temp2 = new String[temp.length - 1];
                        int index = 0;
                        for (int i = 0; i < temp.length; i++) {
                            if (!temp[i].equals(title + "+" + id)) {
                                temp2[index] = temp[i];
                                index++;
                            }
                        }
                        watchList_movieList = String.join("_", temp2);
                    }
                    storeMovieWatchList();
                    checkOnList();
                } else {
                    String[] temp = watchList_movieList.split("_");
                    if (!temp[0].equals("")) {
                        watchList_movieList = String.join("_", temp) + "_" + title + "+" + id;
                    } else {
                        watchList_movieList = title + "+" + id;
                    }
                    storeMovieWatchList();
                    checkOnList();
                }
            }
        });
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
                            if (!temp[i].equals(title + "+" + id)) {
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
                        dislikeList_movieList = String.join("_", temp) + "_" + title + "+" + id;
                    } else {
                        dislikeList_movieList = title + "+" + id;
                    }
                    storeMovieDislikeList();
                    checkOnList();
                }
            }
        });
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter adapter = new CommentAdapter(this, movieCommentList);
        recyclerView_comment.setAdapter(adapter);
        fab_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieCommentList.addComment(userName, EditText_comment.getText().toString());
                storeComment();
                EditText_comment.setText("");
                getUserName_Comment();
                recyclerView_comment.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private void getUserName_Comment() {
        usersInfo.document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> preferences = documentSnapshot.getData();
                    if (preferences != null && preferences.containsKey("userName")) {
                        userName = (String) preferences.get("userName");
                    }
                }
            }
        });
        commentListInfo.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> preferences = documentSnapshot.getData();
                    if (preferences != null && preferences.containsKey("CommentUser")) {
                        CommentUser = (List<String>) preferences.get("CommentUser");
                    } else {
                        CommentUser = new ArrayList<>();
                    }

                    if (preferences != null && preferences.containsKey("CommentDetails")) {
                        CommentDetails = (List<String>) preferences.get("CommentDetails");
                    } else {
                        CommentDetails = new ArrayList<>();
                    }
                    commentList.clear();
                    for (int i = 0; i < CommentUser.size(); i++) {
                        commentList.add(new Comment(CommentUser.get(i), CommentDetails.get(i)));
                    }
                    recyclerView_comment.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    private void storeComment() {
        Map<String, Object> preferenceCollection = new HashMap<>();
        CommentUser.add(userName);
        CommentDetails.add(EditText_comment.getText().toString());
        preferenceCollection.put("CommentUser", CommentUser);
        preferenceCollection.put("CommentDetails", CommentDetails);
        db.collection("Comment List").document(id)
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

    private void checkOnList() {
        if (checkWhetherMovieIsAddedWatchList()) {
            buttonWatchList.setChecked(true);
        }
        if (checkWhetherMovieIsAddedDislikeList()) {
            button_block.setChecked(true);
        }
    }


    private Boolean checkWhetherMovieIsAddedWatchList() {
        String[] temp = watchList_movieList.split("_");
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals(title + "+" + id)) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkWhetherMovieIsAddedDislikeList() {
        String[] temp = dislikeList_movieList.split("_");
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals(title + "+" + id)) {
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