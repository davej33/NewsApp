package com.example.android.newsapp;

/**
 * Created by dnj on 11/8/16.
 */

public class Article {

    private String mTitle;
    private String mSection;
    private String mDate;
    private String mURL;

    public Article(String title, String section, String date, String url){
        mTitle = title;
        mSection = section;
        mDate = date;
        mURL = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmURL() {
        return mURL;
    }
}
