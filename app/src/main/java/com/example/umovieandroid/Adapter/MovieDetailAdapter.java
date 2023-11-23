package com.example.umovieandroid.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umovieandroid.Model.Movie;
import com.example.umovieandroid.MovieActivity;
import com.example.umovieandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.ViewHolder> {
    Context context;
    List<Movie> movieList;
    final String img = "https://image.tmdb.org/t/p/w500";
    final String TAG = "umovie";

    public MovieDetailAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_detail_layout, parent, false);
        MovieDetailAdapter.ViewHolder holder = new MovieDetailAdapter.ViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieActivity.class);
                intent.putExtra("id", movieList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("overview", movieList.get(holder.getAdapterPosition()).getOverview());
                intent.putExtra("backdrop", movieList.get(holder.getAdapterPosition()).getBackdrop_path());
                intent.putExtra("title", movieList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("rating", movieList.get(holder.getAdapterPosition()).getVote_average() + "");
                intent.putExtra("year", movieList.get(holder.getAdapterPosition()).getRelease_date());
                intent.putExtra("vote_count", movieList.get(holder.getAdapterPosition()).getVote_count());
                intent.putExtra("genre", generateGenreList(holder.getAdapterPosition()));
                if (movieList.get(holder.getAdapterPosition()).getSimilarityScores() != -1) {
                    intent.putExtra("score", movieList.get(holder.getAdapterPosition()).getSimilarityScores());
                }
                startActivity(context, intent, null);
            }
        });
        return holder;
    }
    private String generateGenreList(int adapterPosition) {
        String output = "";
        List<String> genreList = movieList.get(adapterPosition).getGenreList();
        for (int i = 0; i < genreList.size(); i++) {
            output += genreList.get(i) + ",";
        }
        return output.substring(0, output.length()-1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView imageView = holder.img_movie;
        Picasso.get().load(img + movieList.get(position).getPoster_path()).into(imageView);
        holder.txt_movie_title.setText(movieList.get(position).getTitle());
        holder.txt_movie_vote_average.setText(movieList.get(position).getVote_average() + "");
        holder.txt_movie_overview.setText(movieList.get(position).getOverview());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_movie;
        TextView txt_movie_title, txt_movie_vote_average, txt_movie_overview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_movie = itemView.findViewById(R.id.img_movie);
            txt_movie_title = itemView.findViewById(R.id.txt_movie_title);
            txt_movie_vote_average = itemView.findViewById(R.id.txt_movie_vote_average);
            txt_movie_overview = itemView.findViewById(R.id.txt_movie_overview);
        }
    }
}
