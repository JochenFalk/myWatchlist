package com.company.data.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Search {

    private long id;
    private String title;
    private String year;
    private int returnValue;

    private static ArrayList<Search> searches = new ArrayList<>();

    private HashMap<String, String> results = new HashMap<>();
    private ArrayList<Object> cast = new ArrayList<>();
    private ArrayList<Object> crew = new ArrayList<>();

    public Search(String title, String year, int returnValue) {
        this.title = title;
        this.year = year;
        this.returnValue = returnValue;
    }

    public Search(String title, String year) {
        this.title = title;
        this.year = year;
        this.returnValue = 0;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    public static ArrayList<Search> getSearches() {
        return searches;
    }

    public static void setSearches(ArrayList<Search> searches) {
        Search.searches = searches;
    }

    public static void addSearches(Search search)
    {
        searches.add(search);
    }

    public HashMap<String, String> getResults() {
        return results;
    }

    public void setResults(HashMap<String, String> results) {
        this.results = results;
    }

    public ArrayList<Object> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Object> cast) {
        this.cast = cast;
    }

    public ArrayList<Object> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Object> crew) {
        this.crew = crew;
    }

    @Override
    public String toString() {
        return  "Title: " + title + "; " +
                "Year: " + year + "; " +
                "Return value: " + returnValue + "; ";
    }
}
