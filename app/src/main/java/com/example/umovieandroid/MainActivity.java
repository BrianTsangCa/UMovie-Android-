package com.example.umovieandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.umovieandroid.Adapter.CardAdapter;
import com.example.umovieandroid.Adapter.HeroAdapter;
import com.example.umovieandroid.LocalDatabase.UMovieDatabase;
import com.example.umovieandroid.Model.Movie;
import com.example.umovieandroid.RegisterLoginAcitivties.PreferenceActivity;
import com.example.umovieandroid.RegisterLoginAcitivties.RegisterLoginActivity;
import com.example.umovieandroid.Vector.Dao.MovieVectorDao;
import com.example.umovieandroid.Vector.Dao.UserVectorDao;
import com.example.umovieandroid.Vector.Model.MovieVector;
import com.example.umovieandroid.Vector.Model.UserVector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
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
    List<Movie> movieList_movieVector = new ArrayList<>();
    List<Movie>[] movielistarray = new List[5];
    List<String> allGenreList = new ArrayList<>();

    JsonObjectRequest jsonObjectRequest, jsonObjectRequest1, jsonObjectRequest2;
    RecyclerView[] recyclerViewArray = new RecyclerView[5];
    TextView[] txtViewArray = new TextView[5];
    HeroAdapter heroAdapter;
    CardAdapter[] adapters = new CardAdapter[5];
    CardAdapter recommendedAdapter = new CardAdapter();
    final String TAG = "umovie";
    List<String> genre = new ArrayList<>();
    TextView txtView0, txtView1, txtView2, txtView3, txtView4;
    RecyclerView recyclerviewRecommended, carousel_recycler_view, recyclerview0, recyclerview1, recyclerview2, recyclerview3, recyclerview4;

    String userEmail;
    SearchView searchView;

    RequestQueue requestQueue;
    List<String> genreList_MovieVector = new ArrayList<>();
    List<String> eraList_MovieVector = new ArrayList<>();
    List<String> idList_MovieVector = new ArrayList<>();
    List<String> movieVector = new ArrayList<>();
    String userVector = "";
    List<Double> similarityScoresList = new ArrayList<>();

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
        recyclerviewRecommended = findViewById(R.id.recyclerviewRecommended);
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
                        List<String> genreList = new ArrayList<>();
                        JSONArray genreArray = Data.getJSONArray("genre_ids");
                        for (int j = 0; j < genreArray.length(); j++) {
                            genreList.add(genreArray.getInt(j) + "");
                        }
                        movielist.add(new Movie(Data.getString("id"), Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Data.getDouble("vote_average"), Double.parseDouble(Data.getString("vote_count")), genreList, Data.getString("backdrop_path")));

                    }
                    txtViewArray[0].setText("Action");
                    movielistarray[0] = movielist;
                    adapters[0] = new CardAdapter(MainActivity.this, movielist);
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
                                                List<String> genreList = new ArrayList<>();
                                                JSONArray genreArray = Data.getJSONArray("genre_ids");
                                                for (int i = 0; i < genreArray.length(); i++) {
                                                    genreList.add(genreArray.getInt(i) + "");
                                                }
                                                movielist.add(new Movie(Data.getString("id"), Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Data.getDouble("vote_average"), Double.parseDouble(Data.getString("vote_count")), genreList, Data.getString("backdrop_path")));
                                            }
                                            txtViewArray[index].setText(genre.get(index));
                                            movielistarray[index] = movielist;
                                            adapters[index] = new CardAdapter(MainActivity.this, movielistarray[index]);
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
                        List<String> genreList = new ArrayList<>();
                        JSONArray genreArray = Data.getJSONArray("genre_ids");
                        for (int j = 0; j < genreArray.length(); j++) {
                            genreList.add(genreArray.getInt(j) + "");
                        }
                        movielist.add(new Movie(Data.getString("id"), Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Data.getDouble("vote_average"), Double.parseDouble(Data.getString("vote_count")), genreList, Data.getString("backdrop_path")));
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
        getMovieVector();
    }

    private void generatePersonalizedMovieLists() {
        String output = "";
        for (int i = 0; i < similarityScoresList.size(); i++) {
            output += similarityScoresList.get(i) + " , ";
        }
    }

    private void calculateSimilarityScores() {
        for (int i = 0; i < movieVector.size(); i++) {
            double output = 0;
            if (userVector.equals("") || movieVector.get(i).equals("")) {
                output = 0;
                output = calculateSimilarityScores(userVector.split(","), movieVector.get(i).split(","));
            } else {
                output = calculateSimilarityScores(userVector.split(","), movieVector.get(i).split(","));
            }
            similarityScoresList.add(output);
            movieList_movieVector.get(i).setSimilarityScores((int) (output * 100));
        }
        Collections.sort(movieList_movieVector);
    }

    private double calculateSimilarityScores(String[] x, String[] y) {
        double output = 0;
        double x_y = 0;
        double x2 = 0;
        double y2 = 0;
        for (int i = 0; i < x.length; i++) {
            x_y += Double.parseDouble(x[i]) * Double.parseDouble(y[i]);
            x2 += Double.parseDouble(x[i]) * Double.parseDouble(x[i]);
            y2 += Double.parseDouble(y[i]) * Double.parseDouble(y[i]);
        }
        output = x_y / Math.sqrt(x2) / Math.sqrt(y2);
        return output;
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
                userVector = arrayToString(a) + "," + arrayToString(b) + "," + arrayToString(c);

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
                    url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&vote_count.gte=10";
                } else {
                    url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&vote_average.gte=" + ratingList.get(0) + "&vote_count.gte=10";
                }
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject Data = results.getJSONObject(i);
                                List<String> genreListtemp = new ArrayList<>();
                                JSONArray genreJsonArray = Data.getJSONArray("genre_ids");
                                for (int j = 0; j < genreJsonArray.length(); j++) {
                                    genreListtemp.add(getKeyByValue(dict, genreJsonArray.getInt(j)));
                                }
                                movieList_movieVector.add(new Movie(Data.getInt("id") + "", Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Data.getDouble("vote_average"), Data.getDouble("vote_count"), genreListtemp, Data.getString("backdrop_path")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        movieVector = new ArrayList<>();
                        List<String> movieTitles = new ArrayList<>();
                        for (int i = 0; i < movieList_movieVector.size(); i++) {
                            genreList_MovieVector = movieList_movieVector.get(i).getGenreList();
                            double[] a = getGenreVector(genreList_MovieVector);
                            int year = Integer.parseInt(movieList_movieVector.get(i).getRelease_date().split("-")[0]);
                            if (year >= 1970 && year <= 1989) {
                                eraList_MovieVector.add("Golden Age Era (1970- 1989)");
                            } else if (year >= 1917 && year <= 1969) {
                                eraList_MovieVector.add("Classic Era (1917-1969)");
                            } else if (year >= 1990 && year <= 2009) {
                                eraList_MovieVector.add("Modern Era (1990-2009)");
                            } else if (year >= 2010 && year <= 2023) {
                                eraList_MovieVector.add("Contemporary Era (2010-2023)");
                            }
                            double[] b = getEraVector(eraList_MovieVector);
                            int id = Integer.parseInt(movieList_movieVector.get(i).getId());
                            List<Integer> BradMovieList = new ArrayList<>();
                            BradMovieList.add(16869);
                            BradMovieList.add(72190);
                            BradMovieList.add(550);
                            List<Integer> LeonardoMovieList = new ArrayList<>();
                            LeonardoMovieList.add(27205);
                            LeonardoMovieList.add(597);
                            LeonardoMovieList.add(11324);
                            LeonardoMovieList.add(27205);
                            if (BradMovieList.contains(id)) {
                                idList_MovieVector.add("Brad Pitt");
                            } else if (LeonardoMovieList.contains(id)) {
                                idList_MovieVector.add("Leonardo DiCaprio");
                            }
                            double[] c = getActorVector(idList_MovieVector);
                            String aMovieVector = arrayToString(a) + "," + arrayToString(b) + "," + arrayToString(c);
                            movieVector.add(aMovieVector);
                            movieTitles.add(movieList_movieVector.get(i).getTitle());
                        }
                        MovieVectorDao movieDao = udb.movieVectorDao();

                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < movieList_movieVector.size(); i++) {
                                    movieDao.insertMovieVector(new MovieVector(movieTitles.get(i), movieVector.get(i)));
                                    calculateSimilarityScores();
                                    generatePersonalizedMovieLists();
                                }
                            }
                        });
                        recyclerviewRecommended.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        recommendedAdapter = new CardAdapter(MainActivity.this, movieList_movieVector);
                        recyclerviewRecommended.setAdapter(recommendedAdapter);
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

    private String getKeyByValue(Dictionary<String, Integer> dict, int i) {
        for (Enumeration<String> keys = dict.keys(); keys.hasMoreElements(); ) {
            String key = keys.nextElement();
            if (dict.get(key).equals(i)) {
                return key;
            }
        }
        return null;
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
        return output;
    }

    public double[] getActorVector(List<String> actorList) {
        double[] output = new double[2];
        List<String> allActorList = new ArrayList<>();
        allActorList.add("Brad Pitt");
        allActorList.add("Leonardo DiCaprio");
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
        return output;
    }
}