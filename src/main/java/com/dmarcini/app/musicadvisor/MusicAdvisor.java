package com.dmarcini.app.musicadvisor;

import com.dmarcini.app.httpserverhandler.HttpRequestHeader;
import com.dmarcini.app.httpserverhandler.HttpServerHandler;

import com.dmarcini.app.musicadvisor.reponse.ResponseView;
import com.dmarcini.app.musicadvisor.request.*;

import com.google.gson.JsonParser;

import java.util.Scanner;

public class MusicAdvisor {
    private final static String CLIENT_ID = "9a6ab59d9c8b42a49901c0732372136d";
    private final static String CLIENT_SECRET = "7e7ced2afefb42b58718e68fae38dbaf";
    private final static String GRANT_TYPE = "authorization_code";
    private final static String REDIRECT_URI = "http://localhost:8080";
    private final static String ACCESS_SERVER_URI = "https://accounts.spotify.com/api/token";

    private final Scanner scanner;

    private final HttpServerHandler httpServerHandler;

    private final RequestsManager requestsManager;
    private final ResponseView responseView;

    public MusicAdvisor() {
        this.scanner = new Scanner(System.in);

        this.httpServerHandler = new HttpServerHandler();

        this.requestsManager = new RequestsManager();
        this.responseView = new ResponseView(5);
    }

    public void menu() {
        MusicAdvisorOption musicAdvisorOption;
        String[] userInput = new String[0];

        do {
            musicAdvisorOption = null;

            while (musicAdvisorOption == null) {
                userInput = scanner.nextLine().split(" ");

                musicAdvisorOption = MusicAdvisorOption.fromString(userInput[0]);
            }

            switch (musicAdvisorOption) {
                case AUTH -> auth();
                case FEATURED_PLAYLISTS -> responseView.setEntries(
                        MusicAdvisorOption.FEATURED_PLAYLISTS,
                        requestsManager.requestFeaturedPlaylists().getFeaturedPlaylists()
                );
                case NEW_ALBUMS -> responseView.setEntries(
                        MusicAdvisorOption.NEW_ALBUMS,
                        requestsManager.requestNewAlbums().getNewAlbums()
                );
                case CATEGORIES -> responseView.setEntries(
                        MusicAdvisorOption.CATEGORIES,
                        requestsManager.requestCategories().getCategories()
                );
                case PLAYLISTS -> {
                    if (userInput.length < 2) {
                        System.out.println("Please specify a category of playlists.");
                        break;
                    }

                    responseView.setEntries(
                            MusicAdvisorOption.PLAYLISTS,
                            requestsManager.requestPlaylists(userInput[1]).getPlaylists()
                    );
                }
                case NEXT_PAGE -> responseView.nextPage();
                case PREV_PAGE -> responseView.prevPage();
            }
        } while (musicAdvisorOption != MusicAdvisorOption.EXIT);
    }

    private void auth() {
        httpServerHandler.connectToServer(8080).startServer();

        System.out.println("Use this link to request the authorization code:");
        System.out.println("https://accounts.spotify.com/authorize?client_id=" + CLIENT_ID +
                           "&redirect_uri=" + REDIRECT_URI + "&response_type=code");

        httpServerHandler.waitOnRequest().stopServer();

        if (httpServerHandler.getQuery().startsWith("error")) {
            System.out.println("Authorization code not found. Try again.");
            return;
        }

        System.out.println("Authorization code received.");

        String requestBody =
                "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=" + GRANT_TYPE +
                "&code=" + httpServerHandler.getQuery().substring(5) +
                "&redirect_uri=" + REDIRECT_URI;
        HttpRequestHeader httpRequestHeader =
                new HttpRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        System.out.println("Making http request for access_token...");

        String response = httpServerHandler.makeHttpPostRequest(ACCESS_SERVER_URI, httpRequestHeader,
                                                                requestBody).getHttpResponse();

        String accessToken = JsonParser.parseString(response).getAsJsonObject().get("access_token").getAsString();

        requestsManager.setAccessToken(accessToken);

        System.out.println("Success!");
    }
}
