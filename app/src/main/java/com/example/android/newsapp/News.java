package com.example.android.newsapp;

import java.util.Date;

/**
 * Created by user on 22/5/2018.
 */

public class News {

    private String name;
    private String date;
    private String title;
    private String url;
    private String author;

    public News(String sectionName, String webPublicationDate, String authorName, String webTitle, String webUrl){
        name = sectionName;
        date = webPublicationDate;
        title = webTitle;
        url = webUrl;
        author = authorName;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }
}
