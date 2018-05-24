package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 24/5/2018.
 */

public final class QueryUtils {

    // Tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the guardian dataset and return a list of {@lin News} objects
     */

    public static List<News> fetchNewsData(String requestUrl){
        //Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}
        List<News> news = extractFeatureFronJson(jsonResponse);

        // Return the list of {@link News}
        return news;
    }

    /**
     * Returns new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful (response code 200)
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //Closing the input stream could throw an IOException, which is why
                //the makeHttpRequest() method signature specifies an IOException could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response
     * from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /** return a list of {@link News} objects that has been built up from parsing the given
     * JSON response.
     */
    private static List<News> extractFeatureFronJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)){
            return null;
        }

        //create an empty ArrayList that we can add news to
        List<News> news = new ArrayList<>();

        //try to parse JSON response string. If there is a problem with the way the JSON is formmated,
        //a JSONException object will be thrown.
        //Catch the exception and print the error message to the logs
        try{
            //create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            //extract the JSONArray associated with the key called "results" which represents
            //list of news results
            JSONArray newsArray = baseJsonResponse.getJSONArray("results");

            //for each news in the newsArray, create a {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {
                //get a single news at posiion i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // extract the value for the key called "sectionName"
                String section = currentNews.getString("sectionName");

                // extract the value for the key called "webPublicationDate"
                String webDate = currentNews.getString("webPublicationDate");

                // extract the value for the key called "webTitle"
                String title = currentNews.getString("webTitle");

                // extract the value for the key called "webUrl"
                String webUrl = currentNews.getString("webUrl");

                //create a new {@link News} object with the section Name, Date, Title and Url
                //from the JSON response

                News newsResponse = new News(section, webDate, title, webUrl);

                //add it to the list of news
                news.add(newsResponse);
            }
        } catch (JSONException e) {
            //if and error is thrown when executing any of the above statement in try block, catch the exception here
            //so the app will not crash.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results.", e);
        }

        return news;
    }
}
