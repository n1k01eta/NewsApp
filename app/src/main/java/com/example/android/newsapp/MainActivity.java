package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /** URL for news data from the guardian dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?order-by=newest&show-tags=contributor&page-size=15&q=politics&api-key=test";

    /**
     * Constant value for the news loader ID. we can choose an integer
     *
     */
    private static final int NEWS_LOADER_ID = 1;

    /** Adapter for the list of news */
    private NewsAdapter adapter;

    // TextView that is displayed when the list is empty
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyStateTextView);

        //create a new adapter that takes an empty list of news as input
        adapter = new NewsAdapter(this, new ArrayList<News>());

        //set the adapter on the ListView
        newsListView.setAdapter(adapter);

        //set an item click listener on the ListView, which sends intent to a web browser to open a website with more
        //information about the selected news
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the current article that was clicked on
                News currentNews = adapter.getItem(position);

                //Convert the String URl into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                //Create a new intent to view the News URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                //Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //Get a reference to the Conectivity manager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //if there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()){
            //get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //initialise the loader. pass in the ID constant defined above and pass in null for the bundle
            //pass in this activity for the LoaderCallbacks parameter
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            //otherwise, display error
            //first, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            //update empty state with no connection error message
            emptyStateTextView.setText(R.string.noInternet);
        }
    }

    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        //hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //set empty state text to display "No News Found."
        emptyStateTextView.setText(R.string.noNews);

        //clear the adapter of previous news data
        adapter.clear();

        //if there is a valid list of {@link News} then add tem to the adapter's data set.
        //this will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
            //updateUi(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}
