package com.dmarcini.app.musicadvisor.userrequest;

import com.google.gson.JsonElement;

public class NewAlbum {
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
