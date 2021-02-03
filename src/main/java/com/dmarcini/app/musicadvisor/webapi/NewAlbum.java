package com.dmarcini.app.musicadvisor.webapi;

import com.google.gson.JsonElement;

public class NewAlbum implements Printable {
    private final String name;
    private final String author;
    private final String url;

    public NewAlbum(JsonElement newAlbum) {
        this.name = newAlbum.getAsJsonObject()
                .get("name").getAsString();
        this.author = newAlbum.getAsJsonObject()
                .get("artists").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("name").getAsString();
        this.url = newAlbum.getAsJsonObject()
                .get("external_urls").getAsJsonObject()
                .get("spotify").getAsString();
    }

    public void print() {
        System.out.println(name);
        System.out.println("[" + author + "]");
        System.out.println(url);
        System.out.println();
    }
}
