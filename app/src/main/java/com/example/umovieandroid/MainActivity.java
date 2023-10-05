package com.example.umovieandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.umovieandroid.Model.Genre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RecyclerView carousel_recycler_view;
    List<String> imglist = new ArrayList<>();
    HeroAdapter heroAdapter;
    List<Genre> genrelist = new ArrayList<>();
    final String TAG = "umovie";
    List<String> genre = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carousel_recycler_view = findViewById(R.id.carousel_recycler_view);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        db.collection("preferences").document(userEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        genre = (List<String>) document.get("genre");
                        if (genre != null) {
                            for (String genreItem : genre) {
                                Log.d(TAG, "Genre: " + genreItem);
                            }
                        } else {
                            Log.d(TAG, "Genre is null or empty.");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Error getting document: " + task.getException());
                }
            }
        });

        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject Data = results.getJSONObject(i);
                                imglist.add(Data.getString("poster_path"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        heroAdapter = new HeroAdapter(MainActivity.this, imglist);
                        carousel_recycler_view.setAdapter(heroAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("error", "Error: " + error.getMessage());
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Add the Authorization header with the Bearer token
                headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjM2U5NGNjMzk3ZTkyYTFlMjdkOWM2YmE2NDAyYWVjMSIsInN1YiI6IjY1MDBmNjg0ZWZlYTdhMDExYWI4MzZlOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.FyDIEJK4BE6pY5GqHJQM0EOCbnii7XmB8NjUy9vonnQ");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
        if(genre.size()==0){
            url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=Action";
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray results = response.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject Data = results.getJSONObject(i);
                                    imglist.add(Data.getString("poster_path"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            heroAdapter = new HeroAdapter(MainActivity.this, imglist);
                            carousel_recycler_view.setAdapter(heroAdapter);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("error", "Error: " + error.getMessage());
                        }
                    }

            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    // Add the Authorization header with the Bearer token
                    headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjM2U5NGNjMzk3ZTkyYTFlMjdkOWM2YmE2NDAyYWVjMSIsInN1YiI6IjY1MDBmNjg0ZWZlYTdhMDExYWI4MzZlOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.FyDIEJK4BE6pY5GqHJQM0EOCbnii7XmB8NjUy9vonnQ");
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }else{
            
            for(int i=0;i<genre.size();i++){
                url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres="+genre.get(i);
                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray results = response.getJSONArray("results");
                                    for (int i = 0; i < results.length(); i++) {
                                        JSONObject Data = results.getJSONObject(i);
                                        imglist.add(Data.getString("poster_path"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                heroAdapter = new HeroAdapter(MainActivity.this, imglist);
                                carousel_recycler_view.setAdapter(heroAdapter);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("error", "Error: " + error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        // Add the Authorization header with the Bearer token
                        headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjM2U5NGNjMzk3ZTkyYTFlMjdkOWM2YmE2NDAyYWVjMSIsInN1YiI6IjY1MDBmNjg0ZWZlYTdhMDExYWI4MzZlOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.FyDIEJK4BE6pY5GqHJQM0EOCbnii7XmB8NjUy9vonnQ");
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            }
        }

    }
}