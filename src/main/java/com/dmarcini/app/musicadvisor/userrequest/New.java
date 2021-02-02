package com.dmarcini.app.musicadvisor.userrequest;

public class New {
    private final String name;
    private final String author;
    private final String url;

    public New(String name, String author, String url) {
        this.name = name;
        this.author = author;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}
