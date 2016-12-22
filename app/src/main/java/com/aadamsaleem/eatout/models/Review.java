package com.aadamsaleem.eatout.models;

/**
 * Created by kirank on 12/22/16.
 */

public class Review {

    // Getter and Setter model for recycler view items
    private String title;
    private int image;

    public Review(String title,  int image) {

        this.title = title;

        this.image = image;
    }

    public String getTitle() {
        return title;
    }



    public int getImage() {
        return image;
    }
}