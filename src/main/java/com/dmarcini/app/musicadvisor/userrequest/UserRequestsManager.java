package com.dmarcini.app.musicadvisor.userrequest;

import com.dmarcini.app.httpserverhandler.HttpRequestHeader;
import com.dmarcini.app.httpserverhandler.HttpServerHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class UserRequestsManager {
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

    public UserRequestsManager() {
        this.httpServerHandler = new HttpServerHandler();

        this.featuredPlaylists = new ArrayList<>();
        this.newAlbums= new ArrayList<>();
        this.categories = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    public void setAccessToken(String accessToken) {
        this.httpRequestHeader = new HttpRequestHeader("Authorization", "Bearer " + accessToken);
    }

    public void printFeaturedPlaylists() {
        for (var featuredPlaylist : featuredPlaylists) {
            System.out.println(featuredPlaylist.getName());
            System.out.println(featuredPlaylist.getUrl());
            System.out.println();
        }
    }

    public void printNewAlbums() {
        for (var newAlbum : newAlbums) {
            System.out.println(newAlbum.getName());
            System.out.println("[" + newAlbum.getAuthor() + "]");
            System.out.println(newAlbum.getUrl());
            System.out.println();
        }
    }

    public void printCategories() {
        for (var category : categories) {
            System.out.println(category.getName());
        }

        System.out.println();
    }

    public void printPlaylists() {
        for (var playlist : playlists) {
            System.out.println(playlist.getName());
            System.out.println(playlist.getUrl());
            System.out.println();
        }
    }

    public UserRequestsManager requestFeaturedPlaylists() {
        if (!featuredPlaylists.isEmpty()) {
            return this;
        }

        var featuredPlaylists = request(FEATURED_PLAYLISTS_URI, "playlists");

        if (featuredPlaylists != null) {
            for (var featuredPlaylist : featuredPlaylists) {
                this.featuredPlaylists.add(new FeaturedPlaylist(featuredPlaylist));
            }
        }

        return this;
    }

    public UserRequestsManager requestNewAlbums() {
        if (!newAlbums.isEmpty()) {
            return this;
        }

        var newAlbums = request(NEW_ALBUMS_URI, "albums");

        if (newAlbums != null) {
            for (var newAlbum : newAlbums) {
                this.newAlbums.add(new NewAlbum(newAlbum));
            }
        }

        return this;
    }

    public UserRequestsManager requestCategories() {
        if (!categories.isEmpty()) {
            return this;
        }

        var categories = request(CATEGORIES_URI, "categories");

        if (categories != null) {
            for (var category : categories) {
                this.categories.add(new Category(category));
            }
        }

        return this;
    }

    public UserRequestsManager requestPlaylists(String categoryName) {
        this.playlists.clear();

        if (categories.isEmpty()) {
            requestCategories();
        }

        String categoryID = findCategoryIDByName(categoryName);

        var playlists =
                request(PLAYLISTS_URI.replace("{category_id}", categoryID), "playlists");

        if (playlists != null) {
            for (var playlist : playlists) {
                this.playlists.add(new Playlist(playlist));
            }
        }

        return this;
    }

    private JsonArray request(String uri, String requestObject) {
        if (httpRequestHeader == null) {
            System.out.println("Please, provide access for application");
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

    private String findCategoryIDByName(String categoryName) {
        var category = categories.stream()
                .filter(c -> c.getName().equals(categoryName))
                .findAny();

        if (category.isPresent()) {
            return category.get().getId();
        }

        return "";
    }
}
