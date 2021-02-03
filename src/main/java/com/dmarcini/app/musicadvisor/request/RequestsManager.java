package com.dmarcini.app.musicadvisor.request;

import com.dmarcini.app.httpserverhandler.HttpRequestHeader;
import com.dmarcini.app.httpserverhandler.HttpServerHandler;

import com.dmarcini.app.musicadvisor.webapi.Category;
import com.dmarcini.app.musicadvisor.webapi.FeaturedPlaylist;
import com.dmarcini.app.musicadvisor.webapi.NewAlbum;
import com.dmarcini.app.musicadvisor.webapi.Playlist;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class RequestsManager {
    private final static String CATEGORIES_URI = "https://api.spotify.com/v1/browse/categories";
    private final static String NEW_ALBUMS_URI = "https://api.spotify.com/v1/browse/new-releases";
    private final static String FEATURED_PLAYLISTS_URI = "https://api.spotify.com/v1/browse/featured-playlists";
    private final static String PLAYLISTS_URI = "https://api.spotify.com/v1/browse/categories/{category_id}/playlists";

    private final HttpServerHandler httpServerHandler;
    private HttpRequestHeader httpRequestHeader;

    private final List<FeaturedPlaylist> featuredPlaylists;
    private final List<NewAlbum> newAlbums;
    private final List<Category> categories;
    private final List<Playlist> playlists;

    public RequestsManager() {
        this.httpServerHandler = new HttpServerHandler();

        this.featuredPlaylists = new ArrayList<>();
        this.newAlbums= new ArrayList<>();
        this.categories = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    public void setAccessToken(String accessToken) {
        this.httpRequestHeader = new HttpRequestHeader("Authorization", "Bearer " + accessToken);
    }

    public List<FeaturedPlaylist> getFeaturedPlaylists() {
        return featuredPlaylists;
    }

    public List<NewAlbum> getNewAlbums() {
        return newAlbums;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public RequestsManager requestFeaturedPlaylists() {
        if (!featuredPlaylists.isEmpty()) {
            return this;
        }

        var featuredPlaylists = request(FEATURED_PLAYLISTS_URI, "playlists");

        if (featuredPlaylists != null) {
            featuredPlaylists.forEach(featuredPlaylist ->
                    this.featuredPlaylists.add(new FeaturedPlaylist(featuredPlaylist)));
        }

        return this;
    }

    public RequestsManager requestNewAlbums() {
        if (!newAlbums.isEmpty()) {
            return this;
        }

        var newAlbums = request(NEW_ALBUMS_URI, "albums");

        if (newAlbums != null) {
            newAlbums.forEach(newAlbum -> this.newAlbums.add(new NewAlbum(newAlbum)));
        }

        return this;
    }

    public RequestsManager requestCategories() {
        if (!categories.isEmpty()) {
            return this;
        }

        var categories = request(CATEGORIES_URI, "categories");

        if (categories != null) {
            categories.forEach(category -> this.categories.add(new Category(category)));
        }

        return this;
    }

    public RequestsManager requestPlaylists(String categoryName) {
        this.playlists.clear();

        if (categories.isEmpty()) {
            requestCategories();
        }

        String categoryID = getCategoryIdByName(categoryName);

        var playlists =
                request(PLAYLISTS_URI.replace("{category_id}", categoryID), "playlists");

        if (playlists != null) {
            playlists.forEach(playlist -> this.playlists.add(new Playlist(playlist)));
        }

        return this;
    }

    private JsonArray request(String uri, String requestObject) {
        if (httpRequestHeader == null) {
            System.out.println("Please, provide access for application.");
            return null;
        }

        String response = httpServerHandler.makeHttpGetRequest(uri, httpRequestHeader).getHttpResponse();

        try {
            return JsonParser.parseString(response).getAsJsonObject()
                    .get(requestObject).getAsJsonObject()
                    .get("items").getAsJsonArray();
        } catch (Exception e) {
            String errorMessage = JsonParser.parseString(response).getAsJsonObject()
                    .get("error").getAsJsonObject()
                    .get("message").getAsString();

            System.out.println(errorMessage);
        }

        return null;
    }

    private String getCategoryIdByName(String categoryName) {
        var category = categories.stream()
                .filter(c -> c.getName().equals(categoryName))
                .findAny();

        return category.isPresent() ? category.get().getId() : "";
    }
}
