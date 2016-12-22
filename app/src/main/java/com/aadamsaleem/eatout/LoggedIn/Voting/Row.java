package com.aadamsaleem.eatout.LoggedIn.Voting;

/**
 * Created by kirank on 12/22/16.
 */

public class Row {

    private final String name;
    private String voteCount;
    private final String restaurantID;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public String getName() {
        return name;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getVoteCount() {
        return this.voteCount;
    }

    public String getRestaurantID() {
        return this.restaurantID;
    }


    public Row(String name, String voteCount, String restaurantID) {
        this.name = name;
        this.isSelected = false;
        this.voteCount = voteCount;
        this.restaurantID = restaurantID;
    }

    @Override
    public String toString() {
        return this.name;
    }



}
