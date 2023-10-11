package com.example.umovieandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.umovieandroid.Adapter.CardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    String _query="";
    RecyclerView recyclerView_searchPage;
    CardAdapter adapter;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    List<String> imgList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchView searchView_searchpage=findViewById(R.id.searchView_searchpage);
        _query = getIntent().getStringExtra("query");
        searchView_searchpage.setQuery(_query, false);
        searchView_searchpage.setIconified(false);
        requestQueue = Volley.newRequestQueue(SearchActivity.this);
        search(_query);
        searchView_searchpage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _query=query;
                search(query);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                _query=newText;
                search(newText);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    public void search(String query){
        String url = "https://api.themoviedb.org/3/search/movie?query="+query+"&include_adult=false&language=en-US&page=1";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    imgList.clear();
                    for (int j = 0; j < results.length(); j++) {
                        JSONObject data = results.getJSONObject(j);
                        imgList.add(data.getString("poster_path"));
                    }
                    recyclerView_searchPage = findViewById(R.id.recyclerView_searchPage);
                    adapter = new CardAdapter(SearchActivity.this, imgList);
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