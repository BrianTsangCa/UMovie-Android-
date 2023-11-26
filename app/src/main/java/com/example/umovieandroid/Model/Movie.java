package com.example.umovieandroid.Model;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Comparable<Movie> {
    private String id;
    private String title;
    private String overview;
    private String poster_path;
    public String backdrop_path;
    private String release_date;
    private int changingSign;
    private double vote_average;
    private double vote_count;
    private List<String> genreList;
    private String movieVector;

    public int getChangingSign() {
        return changingSign;
    }

    private int similarityScores = -1;

    public int getSimilarityScores() {
        return similarityScores;
    }

    public void setSimilarityScores(int similarityScores) {
        if (this.similarityScores == -1 || similarityScores == this.similarityScores) {
            changingSign = 0;
        } else if (similarityScores > this.similarityScores) {
            changingSign = 1;
        } else if (similarityScores < this.similarityScores) {
            changingSign = -1;
        }
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

    public Movie(String id) {
        this.id = id;
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
        int compareSimilarityScore = ((Movie) movie).getSimilarityScores();
        return compareSimilarityScore - this.similarityScores;
    }

    private String getMovieVectorFromMovieList() {
        List<String> genreList_MovieVector = new ArrayList<>();
        List<String> eraList_MovieVector = new ArrayList<>();
        List<String> idList_MovieVector = new ArrayList<>();
        movieVector = "";
        genreList_MovieVector = this.getGenreList();
        double[] a = getGenreVector(genreList_MovieVector);
        int year = Integer.parseInt(this.getRelease_date().split("-")[0]);
        if (year >= 1970 && year <= 1989) {
            eraList_MovieVector.add("Golden Age Era (1970- 1989)");
        } else if (year >= 1917 && year <= 1969) {
            eraList_MovieVector.add("Classic Era (1917-1969)");
        } else if (year >= 1990 && year <= 2009) {
            eraList_MovieVector.add("Modern Era (1990-2009)");
        } else if (year >= 2010 && year <= 2023) {
            eraList_MovieVector.add("Contemporary Era (2010-2023)");
        }
        double[] b = getEraVector(eraList_MovieVector);
        int id = Integer.parseInt(this.getId());
        List<Integer> BradMovieList = new ArrayList<>();
        BradMovieList.add(16869);
        BradMovieList.add(72190);
        BradMovieList.add(550);
        List<Integer> LeonardoMovieList = new ArrayList<>();
        LeonardoMovieList.add(27205);
        LeonardoMovieList.add(597);
        LeonardoMovieList.add(11324);
        if (BradMovieList.contains(id)) {
            idList_MovieVector.add("Brad Pitt");
        } else if (LeonardoMovieList.contains(id)) {
            idList_MovieVector.add("Leonardo DiCaprio");
        }
        double[] c = getActorVector(idList_MovieVector);
        String aMovieVector = arrayToString(a) + "," + arrayToString(b) + "," + arrayToString(c);
        movieVector = aMovieVector;
        return movieVector;
    }

    public String arrayToString(double[] array) {
        String output = "";
        for (int i = 0; i < array.length; i++) {
            output += array[i] + ",";
        }
        return output.substring(0, output.length() - 1);
    }

    public double[] getGenreVector(List<String> genreList) {
        List<String> allGenreList = new ArrayList<>();
        allGenreList.add("Action");
        allGenreList.add("Adventure");
        allGenreList.add("Animation");
        allGenreList.add("Comedy");
        allGenreList.add("Crime");
        allGenreList.add("Documentary");
        allGenreList.add("Drama");
        allGenreList.add("Family");
        allGenreList.add("Fantasy");
        allGenreList.add("History");
        allGenreList.add("Horror");
        allGenreList.add("Music");
        allGenreList.add("Mystery");
        allGenreList.add("Romance");
        allGenreList.add("Science Fiction");
        allGenreList.add("TV Movie");
        allGenreList.add("Thriller");
        allGenreList.add("War");
        allGenreList.add("Western");
        double[] output = new double[19];
        int position = 0;
        for (String genre : allGenreList) {
            if (genreList.contains(genre)) {
                output[position] = 1;
            } else {
                output[position] = 0;
            }
            position++;
        }
        return output;
    }

    public double[] getEraVector(List<String> eraList) {
        double[] output = new double[4];
        List<String> allEraList = new ArrayList<>();
        allEraList.add("Golden Age Era (1970- 1989)");
        allEraList.add("Classic Era (1917-1969)");
        allEraList.add("Modern Era (1990-2009)");
        allEraList.add("Contemporary Era (2010-2023)");
        for (int i = 0; i < allEraList.size(); i++) {
            if (eraList.contains(allEraList.get(i))) {
                output[i] = 1;
            } else {
                output[i] = 0;
            }
        }
        return output;
    }

    public double[] getActorVector(List<String> actorList) {
        double[] output = new double[2];
        List<String> allActorList = new ArrayList<>();
        allActorList.add("Brad Pitt");
        allActorList.add("Leonardo DiCaprio");
        for (int i = 0; i < allActorList.size(); i++) {
            if (actorList.contains(allActorList.get(i))) {
                output[i] = 1;
            } else {
                output[i] = 0;
            }
        }
        return output;
    }
}
