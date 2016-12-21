package com.aadamsaleem.eatout.models;

import java.io.Serializable;

/**
 * Created by aadam on 21/12/2016.
 */

public class Friend implements Serializable{
    public String USER_NAME;
    public String EMAIL_ID;
    public String USER_ID;


    @Override
    public String toString() { return USER_NAME; }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }
}
