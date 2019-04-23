package com.example.qzz;

public class Earthquake {
    //地震震级
    private Double mMagnitude;
    //地震地点
    private String mLocation;
    //地震时间
    private long mTimeInMilliseconds;
    /** 地震的网站 URL */
    private String mUrl;


    public Earthquake(double magnitude,String location, long timeInMilliseconds,String url ){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds =  timeInMilliseconds;
        mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliseconds() { return mTimeInMilliseconds; }

    public String getUrl(){ return mUrl;}
}
