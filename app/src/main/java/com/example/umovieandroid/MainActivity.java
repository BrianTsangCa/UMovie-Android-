package com.example.umovieandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.umovieandroid.Adapter.HeroAdapter;
import com.example.umovieandroid.LocalDatabase.UMovieDatabase;
import com.example.umovieandroid.Model.Movie;
import com.example.umovieandroid.RegisterLoginAcitivties.PreferenceActivity;
import com.example.umovieandroid.RegisterLoginAcitivties.RegisterActivity;
import com.example.umovieandroid.RegisterLoginAcitivties.RegisterLoginActivity;
import com.example.umovieandroid.Vector.Dao.MovieVectorDao;
import com.example.umovieandroid.Vector.Dao.UserVectorDao;
import com.example.umovieandroid.Vector.Model.UserVector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    UMovieDatabase udb;
    Dictionary<String, Integer> dict = new Hashtable<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference preferencesRef = db.collection("preferences");
    CollectionReference usersRef = db.collection("users");

    List<String> genreList = new ArrayList<>();
    List<String> eraList = new ArrayList<>();
    List<String> ratingList = new ArrayList<>();
    List<String> actorList = new ArrayList<>();

    private FirebaseAuth mAuth;
    List<Movie> movielist = new ArrayList<>();
    List<Movie>[] movielistarray = new List[5];
    List<String> allGenreList = new ArrayList<>();

    JsonObjectRequest jsonObjectRequest, jsonObjectRequest1, jsonObjectRequest2;
    RecyclerView[] recyclerViewArray = new RecyclerView[5];
    TextView[] txtViewArray = new TextView[5];
    HeroAdapter heroAdapter;
    HeroAdapter[] adapters = new HeroAdapter[5];
    final String TAG = "umovie";
    List<String> genre = new ArrayList<>();
    TextView txtView0, txtView1, txtView2, txtView3, txtView4;
    RecyclerView carousel_recycler_view, recyclerview0, recyclerview1, recyclerview2, recyclerview3, recyclerview4;

    String userEmail;
    SearchView searchView;

    RequestQueue requestQueue;

    @Override
    public boolean onCreateOptionsMenu(@androidx.annotation.NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setBackgroundColor(Color.WHITE);
                menu.findItem(R.id.profile).setVisible(false);
                menu.findItem(R.id.favorite).setVisible(false);
                menu.findItem(R.id.logout).setVisible(false);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setBackgroundColor(Color.rgb(17, 17, 17));
                menu.findItem(R.id.profile).setVisible(true);
                menu.findItem(R.id.favorite).setVisible(true);
                menu.findItem(R.id.logout).setVisible(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite) {
            Toast.makeText(MainActivity.this, "Favorite Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            Toast.makeText(MainActivity.this, "Search Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
            String output = "Setting";
            intent.putExtra("status", output);
            intent.putExtra("email", userEmail);
            startActivity(intent);

        } else if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            startActivity(new Intent(this, RegisterLoginActivity.class));
            finish();
        } else {
            return false;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dict.put("Action", 28);
        dict.put("Adventure", 12);
        dict.put("Animation", 16);
        dict.put("Comedy", 35);
        dict.put("Crime", 80);
        dict.put("Documentary", 99);
        dict.put("Drama", 18);
        dict.put("Family", 10751);
        dict.put("Fantasy", 14);
        dict.put("History", 36);
        dict.put("Horror", 27);
        dict.put("Music", 10402);
        dict.put("Mystery", 9648);
        dict.put("Romance", 10749);
        dict.put("Science Fiction", 878);
        dict.put("TV Movie", 10770);
        dict.put("Thriller", 53);
        dict.put("War", 10752);
        dict.put("Western", 37);
        allGenreList.add("Action");
        allGenreList.add("Adventure");
        allGenreList.add("Animation");
        allGenreList.add("Comedy");
        allGenreList.add("Crime");
        allGenreList.add("Documentary");
        allGenreList.add("Drama");
        allGenreList.add("Family");
        allGenreList.add("Fantasy");
        allGenreList.add("History");
        allGenreList.add("Horror");
        allGenreList.add("Music");
        allGenreList.add("Mystery");
        allGenreList.add("Romance");
        allGenreList.add("Science Fiction");
        allGenreList.add("TV Movie");
        allGenreList.add("Thriller");
        allGenreList.add("War");
        allGenreList.add("Western");
        carousel_recycler_view = findViewById(R.id.carousel_recycler_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtView0 = findViewById(R.id.txtView0);
        txtView1 = findViewById(R.id.txtView1);
        txtView2 = findViewById(R.id.txtView2);
        txtView3 = findViewById(R.id.txtView3);
        txtView4 = findViewById(R.id.txtView4);
        txtViewArray[0] = txtView0;
        txtViewArray[1] = txtView1;
        txtViewArray[2] = txtView2;
        txtViewArray[3] = txtView3;
        txtViewArray[4] = txtView4;
        recyclerview0 = findViewById(R.id.recyclerview0);
        recyclerview1 = findViewById(R.id.recyclerview1);
        recyclerview2 = findViewById(R.id.recyclerview2);
        recyclerview3 = findViewById(R.id.recyclerview3);
        recyclerview4 = findViewById(R.id.recyclerview4);
        recyclerViewArray[0] = recyclerview0;
        recyclerViewArray[1] = recyclerview1;
        recyclerViewArray[2] = recyclerview2;
        recyclerViewArray[3] = recyclerview3;
        recyclerViewArray[4] = recyclerview4;
        udb = Room.databaseBuilder(
                        getApplicationContext(), UMovieDatabase.class, "umovie.db")
                .build();
        requestQueue = Volley.newRequestQueue(this);
        String url = "";
        url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + dict.get("Action");
        jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    List<Movie> movielist = new ArrayList<>();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject Data = results.getJSONObject(i);
                        movielist.add(new Movie(Data.getString("id"), Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Double.parseDouble(Data.getString("vote_average")), Double.parseDouble(Data.getString("vote_count")), genreList));
                    }
                    txtViewArray[0].setText("Action");
                    movielistarray[0] = movielist;
                    adapters[0] = new HeroAdapter(MainActivity.this, movielist);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewArray[0].setLayoutManager(layoutManager);
                    recyclerview0.setAdapter(adapters[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }
        db.collection("preferences").document(userEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        genre = (List<String>) document.get("genre");
                        if (genre.size() == 0) {
                            requestQueue.add(jsonObjectRequest1);
                        } else {
                            for (int i = 0; i < genre.size() && i < 5; i++) {
                                String url2 = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + dict.get(genre.get(i)) + "&api_key=c3e94cc397e92a1e27d9c6ba6402aec1";
                                int index = i;
                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray results = response.getJSONArray("results");
                                            List<Movie> movielist = new ArrayList<>();
                                            for (int j = 0; j < results.length(); j++) {
                                                JSONObject Data = results.getJSONObject(j);
                                                movielist.add(new Movie(Data.getString("id"), Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Double.parseDouble(Data.getString("vote_average")), Double.parseDouble(Data.getString("vote_count")), genreList));

                                            }
                                            txtViewArray[index].setText(genre.get(index));
                                            movielistarray[index] = movielist;
                                            adapters[index] = new HeroAdapter(MainActivity.this, movielistarray[index]);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                            recyclerViewArray[index].setLayoutManager(layoutManager);
                                            recyclerViewArray[index].setAdapter(adapters[index]);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
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
                                requestQueue.add(jsonObjectRequest2);
                            }
                        }
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

        url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject Data = results.getJSONObject(i);
                        List<String> genreList=new ArrayList<>();
                        movielist.add(new Movie(Data.getString("id"), Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Double.parseDouble(Data.getString("vote_average")), Double.parseDouble(Data.getString("vote_count")), genreList));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                heroAdapter = new HeroAdapter(MainActivity.this, movielist);
                carousel_recycler_view.setAdapter(heroAdapter);
            }
        }, new Response.ErrorListener() {
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
        getUserVector();

    }

    public void getUserVector() {
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

                double[] a = getGenreVector(genreList);
                double[] b = getEraVector(eraList);
                double[] c = getActorVector(actorList);
                String userVector = arrayToString(a) + "," + arrayToString(b) + "," + arrayToString(c);

                MovieVectorDao movieDao = udb.movieVectorDao();
                UserVectorDao userDao = udb.userVectorDao();

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        userDao.insertUserVector(new UserVector(userEmail, userVector));
                    }
                });

            }
        });
    }

    public void getMovieVector() {

        preferencesRef.document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> preferences = documentSnapshot.getData();
                    if (preferences != null && preferences.containsKey("rating")) {
                        ratingList = (List<String>) preferences.get("rating");
                    }
                }
                String url = "";
                if (ratingList.size() == 0) {
                    url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
                } else {
                    url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&vote_average.gte=" + ratingList.get(0);
                }
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
//                                JSONObject Data = results.getJSONObject(i);
//                                List<String> genreListtemp=new ArrayList<>();
//                                List<String> eraListtemp=new ArrayList<>();
//                                List<String> genreListtemp=new ArrayList<>();
//                                genreList_MovieVector.add(Data.getString("genre_ids"));
//                                eraList_MovieVector.add(Data.getString("release_date"));
//                                idList_MovieVector.add(Data.getString("id"));
//                                titleList_MovieVector.add(Data.getString("title"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        double[] a = getGenreVector(genreList_MovieVector);
//                        double[] b = getEraVector(eraList_MovieVector);
//                        double[] c = getActorVector(idList_MovieVector);
//                        String userVector = arrayToString(a) + "," + arrayToString(b) + "," + arrayToString(c);
//
//                        MovieVectorDao movieDao = udb.movieVectorDao();
//
//                        ExecutorService executorService = Executors.newSingleThreadExecutor();
//                        executorService.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                for(int i=0;)
//                                movieDao.insertMovieVector(new UserVector(userEmail, userVector));
//                            }
//                        });
                    }
                }, new Response.ErrorListener() {
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
        });
    }

    public String arrayToString(double[] array) {
        String output = "";
        for (int i = 0; i < array.length; i++) {
            output += array[i] + ",";
        }
        return output.substring(0, output.length() - 1);
    }

    public double[] getGenreVector(List<String> genreList) {
        double[] output = new double[19];
        int position = 0;
        for (String genre : allGenreList) {
            if (genreList.contains(genre)) {
                output[position] = 1;
            } else {
                output[position] = 0;
            }
            position++;
        }
        String toast = "";
        for (int i = 0; i < output.length; i++) {
            toast += output[i] + " ";
        }
        Toast.makeText(this, "Genre vector is :" + toast, Toast.LENGTH_SHORT).show();
        return output;
    }

    public double[] getEraVector(List<String> eraList) {
        double[] output = new double[4];
        List<String> allEraList = new ArrayList<>();
        allEraList.add("Golden Age Era (1970- 1989)");
        allEraList.add("Classic Era (1917-1969)");
        allEraList.add("Modern Era (1990-2009)");
        allEraList.add("Contemporary Era (2010-2023)");
        for (int i = 0; i < allEraList.size(); i++) {
            if (eraList.contains(allEraList.get(i))) {
                output[i] = 1;
            } else {
                output[i] = 0;
            }
        }
        String toast = "";
        for (int i = 0; i < output.length; i++) {
            toast += output[i] + " ";
        }
        Toast.makeText(this, "Era vector is :" + toast, Toast.LENGTH_SHORT).show();
        return output;
    }

    public double[] getActorVector(List<String> actorList) {
        double[] output = new double[2];
        List<String> allActorList = new ArrayList<>();
        allActorList.add("Denzel Washington");
        allActorList.add("Jason Statham");
        for (int i = 0; i < allActorList.size(); i++) {
            if (actorList.contains(allActorList.get(i))) {
                output[i] = 1;
            } else {
                output[i] = 0;
            }
        }
        String toast = "";
        for (int i = 0; i < output.length; i++) {
            toast += output[i] + " ";
        }
        Toast.makeText(this, "Actor vector is :" + toast, Toast.LENGTH_SHORT).show();
        return output;
    }
}