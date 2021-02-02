package com.dmarcini.app.musicadvisor.userrequest;

public class Playlist {
    private final String name;
    private final String url;

    public Playlist(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
