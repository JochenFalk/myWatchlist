package com.company.data.model;

import java.util.ArrayList;

public class Watchlist {

    private long id;
    private String title;
    private String description;
    private ArrayList<Object> listItems;

    private static ArrayList<Watchlist> watchlists = new ArrayList<>();

    public Watchlist(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Watchlist(String title, String description, ArrayList<Object> listItems) {
        this.title = title;
        this.description = description;
        this.listItems = listItems;
    }

    public Watchlist(long id, String title, String description, ArrayList<Object> listItems) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.listItems = listItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public ArrayList getListItems() {
        return listItems;
    }

    public void setListItems(ArrayList<Object> listItems) {
        this.listItems = listItems;
    }

    public static ArrayList<Watchlist> getWatchlists() {
        return watchlists;
    }

    public static void setWatchlists(ArrayList<Watchlist> watchlists) {
        Watchlist.watchlists = watchlists;
    }
}
