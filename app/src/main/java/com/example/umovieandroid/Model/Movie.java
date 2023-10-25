package com.example.umovieandroid.Model;

import java.util.List;

public class Movie implements Comparable<Movie> {
    private String id;
    private String title;
    private String overview;
    private String poster_path;
    public String backdrop_path;
    private String release_date;

    private double vote_average;
    private double vote_count;
    private List<String> genreList;
    private int similarityScores;

    public int getSimilarityScores() {
        return similarityScores;
    }

    public void setSimilarityScores(int similarityScores) {
        this.similarityScores = similarityScores;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getVote_count() {
        return vote_count;
    }

    public void setVote_count(double vote_count) {
        this.vote_count = vote_count;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<String> genreList) {
        this.genreList = genreList;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Movie(String id, String title, String overview, String poster_path, String release_date, double vote_average, double vote_count, List<String> genreList, String backdrop_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.genreList = genreList;
        this.backdrop_path = backdrop_path;
    }

    @Override
    public int compareTo(Movie movie) {
        int compareSimilarityScore
                = ((Movie) movie).getSimilarityScores();
        return this.similarityScores - compareSimilarityScore;
    }
}
