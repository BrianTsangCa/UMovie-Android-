package com.example.umovieandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.umovieandroid.Adapter.CardAdapter;
import com.example.umovieandroid.Adapter.MovieDetailAdapter;
import com.example.umovieandroid.Model.Movie;
import com.example.umovieandroid.RegisterLoginAcitivties.PreferenceActivity;
import com.example.umovieandroid.RegisterLoginAcitivties.RegisterLoginActivity;
import com.example.umovieandroid.Vector.Dao.MovieVectorDao;
import com.example.umovieandroid.Vector.Dao.UserVectorDao;
import com.example.umovieandroid.Vector.Model.UserVector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatchListActivity extends AppCompatActivity {
    SearchView searchView;
    String userEmail;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    RecyclerView recyclerView_watchlist;
    CollectionReference watchListRef = db.collection("Watch List");
    List<Movie> watchListMovieList = new ArrayList<>();
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
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
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(WatchListActivity.this, SearchActivity.class);
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
            startActivity(new Intent(this, WatchListActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return true;
        } else if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(WatchListActivity.this, PreferenceActivity.class);
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
        setContentView(R.layout.activity_watch_list);
        Toolbar toolbar = findViewById(R.id.toolbar_watchList);
        setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        if (user != null) {
            userEmail = user.getEmail();
        }
        requestQueue = Volley.newRequestQueue(this);
        generateWatchListMovie();
    }

    private void generateWatchListMovie() {
        recyclerView_watchlist = findViewById(R.id.recyclerView_watchlist);
        recyclerView_watchlist.setLayoutManager(new LinearLayoutManager(this));
        watchListRef.document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> watchList = documentSnapshot.getData();
                    if (watchList != null && watchList.containsKey("Watch List")) {
                        String watchListData = watchList.get("Watch List").toString();

                        String[] data = watchListData.split("_");
                        for (int i = 0; i < data.length; i++) {
                            String id = data[i].substring(data[i].indexOf("+") + 1, data[i].length());
                            watchListMovieList.add(new Movie(id));
                        }
                    }
                    MovieDetailAdapter adapter = new MovieDetailAdapter(WatchListActivity.this, watchListMovieList);
                    recyclerView_watchlist.setAdapter(adapter);
                }
                getDataFromExternalAPI();
            }
        });
    }

    private void getDataFromExternalAPI() {
        for (int i = 0; i < watchListMovieList.size(); i++) {
            String url = "https://api.themoviedb.org/3/movie/" + watchListMovieList.get(i).getId() + "?language=en-US";
            Movie movie = watchListMovieList.get(i);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        movie.setTitle(response.getString("original_title"));
                        movie.setOverview(response.getString("overview"));
                        movie.setPoster_path(response.getString("poster_path"));
                        movie.setRelease_date(response.getString("release_date"));
                        movie.setVote_average(response.getDouble("vote_average"));
                        movie.setVote_count(response.getDouble("vote_count"));
                        JSONArray genres = response.getJSONArray("genres");
                        List<String> genreList = new ArrayList<>();
                        for (int j = 0; j < genres.length(); j++) {
                            genreList.add(genres.getJSONObject(j).getString("name"));
                        }
                        movie.setGenreList(genreList);
                        movie.setBackdrop_path(response.getString("backdrop_path"));
                        recyclerView_watchlist.getAdapter().notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(WatchListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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