package com.dmarcini.app.musicadvisor.webapi;

import com.google.gson.JsonElement;

public class Playlist implements Printable {
    private final String name;
    private final String url;

    public Playlist(JsonElement playlist) {
        this.name = playlist.getAsJsonObject()
                .get("name").getAsString();
        this.url = playlist.getAsJsonObject()
                .get("external_urls").getAsJsonObject()
                .get("spotify").getAsString();
    }

    public void print() {
        System.out.println(name);
        System.out.println(url);
        System.out.println();
    }
}
