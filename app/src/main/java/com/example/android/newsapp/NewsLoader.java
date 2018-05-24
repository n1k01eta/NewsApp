package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by user on 24/5/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL*/
    private String url;

    /** Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on background thread
     */

    @Override
    public List<News> loadInBackground() {
        if (url == null){
            return null;
        }
        // Perform the network request, parse the response, and extract a list of enews.
        List<News> news = QueryUtils.fetchNewsData(url);

        return news;
    }

}
