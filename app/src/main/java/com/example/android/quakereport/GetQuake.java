package com.example.android.quakereport;

/**
 * Created by Administrator on 8/29/2017.
 */

public class GetQuake {
    private Double mMagnitude;
    private String mLocation;
    private Long mTime;
    private String mUrl;

    //Constructor to initiate the private variables from another class
    public GetQuake (Double magnitude, String location, Long time, String url) {
        mMagnitude = magnitude;
        mLocation =location;
        mTime = time;
        mUrl = url;
    }

    //Get method to get the current value of the magnitude
    public Double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public Long getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }

}
