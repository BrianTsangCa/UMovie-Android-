package com.example.umovieandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    TextView txt_title, txt_rating, txt_year, txt_overview;
    ImageView imgView_movie_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txt_title = findViewById(R.id.txt_title);
        txt_rating = findViewById(R.id.txt_rating);
        txt_year = findViewById(R.id.txt_year);
        txt_overview = findViewById(R.id.txt_overview);
        imgView_movie_pic = findViewById(R.id.imgView_movie_pic);
        String overview = getIntent().getStringExtra("overview");
        String backdrop = "" + getIntent().getStringExtra("backdrop");
        String title = getIntent().getStringExtra("title");
        String rating = getIntent().getStringExtra("rating");
        String year = getIntent().getStringExtra("year");
        int score = getIntent().getIntExtra("score", -1);
        txt_title.setText(title);
        txt_rating.setText("Rating: " + rating);
        if (score != -1) {
            txt_year.setText(score + " % Matches " + "Year: " + year.substring(0, 4));
        } else {
            txt_year.setText("Year: " + year.substring(0, 4));
        }

        txt_overview.setText(overview);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + backdrop).into(imgView_movie_pic);
    }
}