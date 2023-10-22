package com.example.myapplication;

public class Song {
    private int id;
    private String title;
    private String author;
    private String url;

    public Song() {
    }

    public Song(String title, String author, String url) {
        this.title = title;
        this.author = author;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
// Getters y setters
}
