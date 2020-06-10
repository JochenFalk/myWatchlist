package com.company.data;

public class Search {

    private String title;
    private int year;
    private int id;

    public Search(String title, int year) {
        this.title = title;
        this.year = year;
//        this.id = id;
    }

    public Search(String title) {
        this.title = title;
    }

    public Search(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
