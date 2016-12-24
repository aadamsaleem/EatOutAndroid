package com.aadamsaleem.eatout.models;

/**
 * Created by kirank on 12/22/16.
 */

public class Review {

    // Getter and Setter model for recycler view items
    private String title;
    private String review;
    private float rating;
//    private int image;

    public Review(String title,  String review, float rating) {

        this.title = title;

        this.review = review;

        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }



    public String getReview() {
        return this.review;
    }

    public float getRating() {
        return rating;
    }
}