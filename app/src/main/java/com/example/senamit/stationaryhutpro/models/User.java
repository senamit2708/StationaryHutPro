package com.example.senamit.stationaryhutpro.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    //   private String username;
    private String mobileNumber;

    public User(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public User() {
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
