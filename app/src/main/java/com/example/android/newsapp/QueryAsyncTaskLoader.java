package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by dnj on 11/8/16.
 */

public class QueryAsyncTaskLoader extends AsyncTaskLoader {

    private String mURL;

    public QueryAsyncTaskLoader(Context context, String url){
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {

        List<Article> articleList = QueryUtils.fetchData(mURL);
        return articleList;
    }
}
