package com.example.umovieandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Dictionary<String, Integer> dict = new Hashtable<>();


    private FirebaseAuth mAuth;
    List<String> imglist = new ArrayList<>();

    List<String>[] imglistarray = new List[5];

    JsonObjectRequest jsonObjectRequest, jsonObjectRequest1, jsonObjectRequest2;
    RecyclerView[] recyclerViewArray = new RecyclerView[5];
    TextView[] txtViewArray = new TextView[5];
    HeroAdapter heroAdapter;
    HeroAdapter[] adapters = new HeroAdapter[5];
    final String TAG = "umovie";
    List<String> genre = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtView0, txtView1, txtView2, txtView3, txtView4;
    RecyclerView carousel_recycler_view, recyclerview0, recyclerview1, recyclerview2, recyclerview3, recyclerview4;

    String userEmail;

    @Override
    public boolean onCreateOptionsMenu(@androidx.annotation.NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        dict.put("Action", 28);
        dict.put("Adventure", 12);
        dict.put("Crime", 80);
        dict.put("Documentary", 99);
        dict.put("Family", 10751);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carousel_recycler_view = findViewById(R.id.carousel_recycler_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_search_24);
        }

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


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "";                                                      
        url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres="+dict.get("Action");
        jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    ArrayList<String> imgList = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject Data = results.getJSONObject(i);
                        imgList.add(Data.getString("poster_path"));
                    }
                    txtViewArray[0].setText(genre.get(0));
                    imglistarray[0] = imgList;
                    adapters[0] = new HeroAdapter(MainActivity.this, imgList);
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
                                String url2 = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + dict.get(genre.get(i))+"&api_key=c3e94cc397e92a1e27d9c6ba6402aec1";
                                int index = i;
                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray results = response.getJSONArray("results");
                                            ArrayList<String> imgList = new ArrayList<>();
                                            for (int j = 0; j < results.length(); j++) {
                                                JSONObject data = results.getJSONObject(j);
                                                imgList.add(data.getString("poster_path"));
                                            }
                                            txtViewArray[index].setText(genre.get(index));
                                            imglistarray[index] = imgList;
                                            adapters[index] = new HeroAdapter(MainActivity.this, imglistarray[index]);
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
                        imglist.add(Data.getString("poster_path"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                heroAdapter = new HeroAdapter(MainActivity.this, imglist);
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
    }
}