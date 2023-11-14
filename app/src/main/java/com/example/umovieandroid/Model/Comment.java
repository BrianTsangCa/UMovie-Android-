package com.example.umovieandroid.Model;

public class Comment {
    private String userName;
    private String commentDetails;

    public Comment() {
    }

    public Comment(String userName, String commentDetails) {
        this.userName = userName;
        this.commentDetails = commentDetails;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentDetails() {
        return commentDetails;
    }

    public void setCommentDetails(String commentDetails) {
        this.commentDetails = commentDetails;
    }
}
