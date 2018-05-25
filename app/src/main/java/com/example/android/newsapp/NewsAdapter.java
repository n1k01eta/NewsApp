package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 23/5/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private final static String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(Context context, List<News> news){
        super(context, 0, news);
    }

    /** Returns a list item view that displays the news
     *
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Check if the is an existing list item view that we can reuse
        //otherwise, if convert is null, then inflate a new list item view
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        //finds the news at the given position in the list of News
        News currentNews = getItem(position);
        listItemView.findViewById(R.id.item).setBackgroundColor(getSectionColor(currentNews.getName()));
        //find theTextView with the ID section
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        //display the section name in that TextView
        sectionView.setText(currentNews.getName());

        //find the TextView with the ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        //Display the title in that TextView
        titleView.setText(currentNews.getTitle());

        //create a new SimpleDateFormat Object from the date in format "EEE, MMM d, ''yy"

        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EE dd MMM yyyy", Locale.ENGLISH);
        String date = "";
        try {
            Date dateNews = dateFormatJSON.parse(currentNews.getDate());
            date = dateFormat2.format(dateNews);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //find the TextView with the view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        //Display the date of the current news in that TextView
        dateView.setText(date);


        return listItemView;
    }


    /** Return the background color for the section name based on the type of news
     *
      * @param section name of the news
     * @return sectionColorRessourceID
     */
    private int getSectionColor(String section){

        int sectionColorRessourceID;
        switch (section) {
            case "Politics":
                sectionColorRessourceID = R.color.backgroundPolitics;
                break;
            case "Opinion":
                sectionColorRessourceID = R.color.backgroundOpinion;
                break;
            case "Society":
                sectionColorRessourceID = R.color.backgroundSociety;
                break;
            case "Life and Style":
                sectionColorRessourceID = R.color.backgroundLifeAndStyle;
                break;
            case "Environment":
                sectionColorRessourceID = R.color.backgroundEnvironment;
                break;
            default:
                sectionColorRessourceID = R.color.backgroundDefault;
                break;
        }

        return ContextCompat.getColor(getContext(), sectionColorRessourceID);
    }

    /**
     * Return the parsed date
     */
    private Date parseDate(String dateString){
        DateFormat df = new SimpleDateFormat("LLL dd, yyyy");
        Date dateObject = null;
        try {
            dateObject = df.parse(dateString);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parse Date problem", e);
        }

        return dateObject;
    }


}
