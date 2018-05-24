package com.example.android.newsapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by user on 23/5/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

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

        //find theTextView with the ID section
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        //display the section name in that TextView
        sectionView.setText(currentNews.getName());
        //set the proper background color for the section name
        //fetch the background from the TextView, which is a GradientDrawable
        GradientDrawable sectionBackground = (GradientDrawable) sectionView.getBackground();
        //get the appropriate background color based on the current section name
        int sectionColor = getSectionColor(currentNews.getName());
        //set the background color on the section name
        sectionBackground.setColor(sectionColor);

        //find the TextView with the ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        //Display the title in that TextView
        titleView.setText(currentNews.getTitle());

        //create a new SimpleDateFormat Object from the date in format "EEE, MMM d, ''yy"
        SimpleDateFormat dateObject = new SimpleDateFormat("EEE, MMM d, ''yy");
        String date = dateObject.format(currentNews.getDate());

        //find the TextView with the view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        //Display the date of the current news in that TextView
        dateView.setText(date);

        //create a new SimpleDateFormat object from the date in format "h:mm a"
        SimpleDateFormat timeObject = new SimpleDateFormat("h:mm a");
        String time = timeObject.format(currentNews.getDate());

        //find the TextView with the view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        //Display the time in the current news in that TextView
        timeView.setText(time);

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
            default:
                sectionColorRessourceID = R.color.colorPrimaryDark;
                break;
        }

        return ContextCompat.getColor(getContext(), sectionColorRessourceID);
    }
}
