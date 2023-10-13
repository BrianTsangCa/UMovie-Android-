package com.example.umovieandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.umovieandroid.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    String _query = "";
    RecyclerView recyclerView_searchPage;
    CardAdapter adapter;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    SearchView searchView;
    List<Movie> movieList = new ArrayList<>();
    boolean flag = true;

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
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
        if (flag) {
            searchView.setQuery(_query, true);
            searchView.setIconified(false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _query = query;
                search(query);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _query = newText;
                search(newText);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar_searchpage = findViewById(R.id.toolbar_searchpage);
        setSupportActionBar(toolbar_searchpage);
        _query = getIntent().getStringExtra("query");

        requestQueue = Volley.newRequestQueue(SearchActivity.this);
        search(_query);
    }

    public void search(String query) {
        String url = "https://api.themoviedb.org/3/search/movie?query=" + query + "&include_adult=false&language=en-US&page=1";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    movieList.clear();
                    for (int j = 0; j < results.length(); j++) {
                        JSONObject Data = results.getJSONObject(j);
                        List<String> genreList=new ArrayList<>();
                        JSONArray genreArray=Data.getJSONArray("genre_ids");
                        for(int i=0;i<genreArray.length();i++){
                            genreList.add(genreArray.getInt(i)+"");
                        }
                        movieList.add(new Movie(Data.getString("id"), Data.getString("title"), Data.getString("overview"), Data.getString("poster_path"), Data.getString("release_date"), Data.getDouble("vote_average"), Double.parseDouble(Data.getString("vote_count")), genreList,Data.getString("backdrop_path")));
                    }
                    recyclerView_searchPage = findViewById(R.id.recyclerView_searchPage);
                    adapter = new CardAdapter(SearchActivity.this, movieList);
                    recyclerView_searchPage.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
                    recyclerView_searchPage.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error", "Error: " + error.getMessage());
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjM2U5NGNjMzk3ZTkyYTFlMjdkOWM2YmE2NDAyYWVjMSIsInN1YiI6IjY1MDBmNjg0ZWZlYTdhMDExYWI4MzZlOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.FyDIEJK4BE6pY5GqHJQM0EOCbnii7XmB8NjUy9vonnQ");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}