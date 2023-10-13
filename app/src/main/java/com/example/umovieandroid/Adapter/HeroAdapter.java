package com.example.umovieandroid.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umovieandroid.MainActivity;
import com.example.umovieandroid.Model.Movie;
import com.example.umovieandroid.MovieActivity;
import com.example.umovieandroid.R;
import com.example.umovieandroid.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> {
    Context context;
    List<Movie> movieList;
    final String img = "https://image.tmdb.org/t/p/w500";
    final String TAG = "umovie";

    public HeroAdapter() {
    }

    public HeroAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carousel_layout, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MovieActivity.class);
                intent.putExtra("overview",movieList.get(holder.getAdapterPosition()).getOverview());
                intent.putExtra("backdrop",movieList.get(holder.getAdapterPosition()).getBackdrop_path());
                intent.putExtra("title",movieList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("rating",movieList.get(holder.getAdapterPosition()).getVote_average()+"");
                intent.putExtra("year",movieList.get(holder.getAdapterPosition()).getRelease_date());
                startActivity(context,intent,null);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView imageView = holder.carousel_image_view;
        Picasso.get().load(img + movieList.get(position).getPoster_path()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carousel_image_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carousel_image_view = itemView.findViewById(R.id.carousel_image_view);
        }
    }
}
