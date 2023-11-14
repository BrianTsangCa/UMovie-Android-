package com.example.umovieandroid.Model;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class MovieComment {
    int id;
    List<Comment> commentList = new ArrayList<>();

    public MovieComment(int id, List<Comment> commentList) {
        this.id = id;
        this.commentList = commentList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void addComment(String userName, String commentDetails) {
        commentList.add(new Comment(userName, commentDetails));
    }
}
