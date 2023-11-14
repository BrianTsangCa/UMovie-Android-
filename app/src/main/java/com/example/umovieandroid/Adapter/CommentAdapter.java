package com.example.umovieandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umovieandroid.Model.Comment;
import com.example.umovieandroid.Model.MovieComment;
import com.example.umovieandroid.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    MovieComment movieCommentList ;
    final String TAG = "umovie";

    public CommentAdapter() {
    }

    public CommentAdapter(Context context, MovieComment movieCommentList) {
        this.context = context;
        this.movieCommentList = movieCommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_layout, parent, false);
        CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtView_username.setText(movieCommentList.getCommentList().get(position).getUserName());
        holder.txtView_commentDetails.setText(movieCommentList.getCommentList().get(position).getCommentDetails());
    }

    @Override
    public int getItemCount() {
        return movieCommentList.getCommentList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtView_username, txtView_commentDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView_username = itemView.findViewById(R.id.txtView_username);
            txtView_commentDetails = itemView.findViewById(R.id.txtView_commentDetails);
        }
    }
}
