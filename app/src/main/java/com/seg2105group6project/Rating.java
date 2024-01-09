package com.seg2105group6project;


public class Rating {
    private double rating;
    private String comment;

    public Rating(double rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Rating() {}

    public double getRating() { return rating; }
    public String getComment() { return comment; }
}
