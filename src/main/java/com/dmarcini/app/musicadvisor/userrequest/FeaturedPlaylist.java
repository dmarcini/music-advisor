package com.dmarcini.app.musicadvisor.userrequest;

import com.google.gson.JsonElement;

public class FeaturedPlaylist {
    private final String name;
    private final String url;

    public FeaturedPlaylist(JsonElement featuredPlaylist) {
        this.name = featuredPlaylist.getAsJsonObject()
                .get("name").getAsString();
        this.url = featuredPlaylist.getAsJsonObject()
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
