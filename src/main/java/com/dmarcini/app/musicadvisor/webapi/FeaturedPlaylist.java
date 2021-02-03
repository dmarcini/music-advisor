package com.dmarcini.app.musicadvisor.webapi;

import com.google.gson.JsonElement;

public class FeaturedPlaylist implements Printable {
    private final String name;
    private final String url;

    public FeaturedPlaylist(JsonElement featuredPlaylist) {
        this.name = featuredPlaylist.getAsJsonObject()
                .get("name").getAsString();
        this.url = featuredPlaylist.getAsJsonObject()
                .get("external_urls").getAsJsonObject()
                .get("spotify").getAsString();
    }

    public void print() {
        System.out.println(name);
        System.out.println(url);
        System.out.println();
    }
}
