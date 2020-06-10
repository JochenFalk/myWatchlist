package com.company.data;

import java.util.ArrayList;

public class Watchlist {

    private String title;
    private String description;

    private User creator;

    private static ArrayList<Watchlist> watchlists = new ArrayList<>();

    public Watchlist(String title, String description, User creator) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        watchlists.add(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public static ArrayList<Watchlist> getWatchlists() {
        return watchlists;
    }

    public static void setWatchlists(ArrayList<Watchlist> watchlists) {
        Watchlist.watchlists = watchlists;
    }
}
