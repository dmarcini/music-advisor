package com.dmarcini.app.musicadvisor.userrequest;

import com.google.gson.JsonElement;

public class Playlist {
    private final String name;
    private final String url;

    public Playlist(JsonElement playlist) {
        this.name = playlist.getAsJsonObject()
                .get("name").getAsString();
        this.url = playlist.getAsJsonObject()
                .get("external_urls").getAsJsonObject()
                .get("spotify").getAsString();
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
