package com.dmarcini.app.musicadvisor;

import com.dmarcini.app.httpserverhandler.HttpRequestHeader;
import com.dmarcini.app.httpserverhandler.HttpServerHandler;
import com.dmarcini.app.musicadvisor.userrequest.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MusicAdvisor {
    private final static String CLIENT_ID = "9a6ab59d9c8b42a49901c0732372136d";
    private final static String CLIENT_SECRET = "7e7ced2afefb42b58718e68fae38dbaf";
    private final static String GRANT_TYPE = "authorization_code";
    private final static String REDIRECT_URI = "http://localhost:8080";
    private final static String ACCESS_SERVER_URI = "https://accounts.spotify.com/api/token";

    private final static String CATEGORIES_URI = "https://api.spotify.com/v1/browse/categories";
    private final static String NEW_URI = "https://api.spotify.com/v1/browse/new-releases";
    private final static String FEATURED_URI = "https://api.spotify.com/v1/browse/featured-playlists";
    private final static String PLAYLIST_URI = "https://api.spotify.com/v1/browse/categories/{category_id}/playlists";

    private final Scanner scanner;

    private final HttpServerHandler httpServerHandler;

    private String accessToken;

    public MusicAdvisor() {
        this.scanner = new Scanner(System.in);
        this.httpServerHandler = new HttpServerHandler();
    }

    public void menu() {
        Request request;

        do {
            request = null;

            while (request == null) {
                request = Request.fromString(scanner.next());
            }

            switch (request) {
                case AUTH -> auth();
                case FEATURED -> runIfAuthSuccess(this::getFeatured);
                case NEW -> runIfAuthSuccess(this::getNew);
                case CATEGORIES -> runIfAuthSuccess(this::getCategories);
                case PLAYLISTS -> runIfAuthSuccess(this::getPlaylists);
            }
        } while (request != Request.EXIT);
    }

    private void auth() {
        httpServerHandler.connectToServer(8080).startServer();

        System.out.println("use this link to request the access code:");
        System.out.println("https://accounts.spotify.com/authorize?client_id=" + CLIENT_ID +
                           "&redirect_uri=" + REDIRECT_URI + "&response_type=code");

        httpServerHandler.waitOnRequest().stopServer();

        if (httpServerHandler.getQuery().startsWith("error")) {
            System.out.println("Authorization code not found. Try again.");
            return;
        }

        String requestBody =
                "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=" + GRANT_TYPE +
                "&code=" + httpServerHandler.getQuery().substring(5) +
                "&redirect_uri=" + REDIRECT_URI;
        HttpRequestHeader httpRequestHeader =
                new HttpRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        String response =
                httpServerHandler.makeHttpPostRequest(ACCESS_SERVER_URI, httpRequestHeader, requestBody).getHttpResponse();

        accessToken = JsonParser.parseString(response).getAsJsonObject().get("access_token").getAsString();
    }

    private void runIfAuthSuccess(Runnable toRun) {
        if (accessToken != null) {
            toRun.run();
        } else {
            System.out.println("Please, provide access for application");
        }
    }

    private List<Featured> getFeatured() {
        HttpRequestHeader httpRequestHeader =
                new HttpRequestHeader("Authorization", "Bearer " + accessToken);

        String response =
                httpServerHandler.makeHttpGetRequest(FEATURED_URI, httpRequestHeader).getHttpResponse();

        JsonArray playlists = JsonParser.parseString(response).getAsJsonObject()
                .get("playlists").getAsJsonObject()
                .get("items").getAsJsonArray();

        List<Featured> featured = new ArrayList<>();

        for (var playlist : playlists) {
            String name = playlist.getAsJsonObject().get("name").getAsString();
            String url = playlist.getAsJsonObject().get("external_urls").getAsJsonObject()
                    .get("spotify").getAsString();

            featured.add(new Featured(name, url));
        }

        return featured;
    }

    private List<New> getNew() {
        HttpRequestHeader httpRequestHeader =
                new HttpRequestHeader("Authorization", "Bearer " + accessToken);

        String response =
                httpServerHandler.makeHttpGetRequest(NEW_URI, httpRequestHeader).getHttpResponse();

        JsonArray newAlbums = JsonParser.parseString(response).getAsJsonObject()
                .get("albums").getAsJsonObject()
                .get("items").getAsJsonArray();

        List<New> newAlbumsList = new ArrayList<>();

        for (var newAlbum : newAlbums) {
            String name = newAlbum.getAsJsonObject().get("name").getAsString();
            String author = newAlbum.getAsJsonObject().get("artists").getAsJsonArray()
                    .get(0).getAsJsonObject()
                    .get("name").getAsString();
            String url = newAlbum.getAsJsonObject().get("external_urls").getAsJsonObject()
                    .get("spotify").getAsString();

            newAlbumsList.add(new New(name, author, url));
        }

        return newAlbumsList;
    }

    private List<Category> getCategories() {
        HttpRequestHeader httpRequestHeader =
                new HttpRequestHeader("Authorization", "Bearer " + accessToken);

        String response =
                httpServerHandler.makeHttpGetRequest(CATEGORIES_URI, httpRequestHeader).getHttpResponse();

        JsonArray categories = JsonParser.parseString(response).getAsJsonObject()
                .get("categories").getAsJsonObject()
                .get("items").getAsJsonArray();

        List<Category> categoriesList = new ArrayList<>();

        for (var category : categories) {
            String id = category.getAsJsonObject().get("id").getAsString();
            String name = category.getAsJsonObject().get("name").getAsString();

            categoriesList.add(new Category(id, name));
        }

        return categoriesList;
    }

    private List<Playlist> getPlaylists() {
        HttpRequestHeader httpRequestHeader =
                new HttpRequestHeader("Authorization", "Bearer " + accessToken);

        String categoryID = "romance";

        String response =
                httpServerHandler.makeHttpGetRequest(PLAYLIST_URI.replace("{category_id}",
                        categoryID), httpRequestHeader).getHttpResponse();

        if (response.contains("Specified id doesn't exist")) {
            System.out.println("Unknown category name.");

            return new ArrayList<>();
        }

        JsonArray playlists = JsonParser.parseString(response).getAsJsonObject()
                .get("playlists").getAsJsonObject()
                .get("items").getAsJsonArray();

        List<Playlist> playlistList = new ArrayList<>();

        for (var playlist : playlists) {
            String name = playlist.getAsJsonObject().get("name").getAsString();
            String url = playlist.getAsJsonObject().get("external_urls").getAsJsonObject()
                    .get("spotify").getAsString();

            playlistList.add(new Playlist(name, url));
        }

        return playlistList;
    }
}
