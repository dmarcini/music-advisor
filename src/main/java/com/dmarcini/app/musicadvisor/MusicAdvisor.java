package com.dmarcini.app.musicadvisor;

import com.dmarcini.app.httpserverhandler.HttpRequestHeader;
import com.dmarcini.app.httpserverhandler.HttpServerHandler;

import com.dmarcini.app.musicadvisor.userrequest.*;

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

    private final UserRequestsManager userRequestsManager;

    public MusicAdvisor() {
        this.scanner = new Scanner(System.in);
        this.httpServerHandler = new HttpServerHandler();
        this.userRequestsManager = new UserRequestsManager();
    }

    public void menu() {
        UserRequest userRequest;
        String[] userInput = new String[0];

        do {
            userRequest = null;

            while (userRequest == null) {
                userInput = scanner.nextLine().split(" ");

                userRequest = UserRequest.fromString(userInput[0]);
            }

            switch (userRequest) {
                case AUTH -> auth();
                case FEATURED_PLAYLISTS -> userRequestsManager.requestFeaturedPlaylists().printFeaturedPlaylists();
                case NEW_ALBUMS -> userRequestsManager.requestNewAlbums().printNewAlbums();
                case CATEGORIES -> userRequestsManager.requestCategories().printCategories();
                case PLAYLISTS -> {
                    if (userInput.length < 2) {
                        System.out.println("Please specify a category of playlists.");
                        break;
                    }

                    userRequestsManager.requestPlaylists(userInput[1]).printPlaylists();
                }
            }
        } while (userRequest != UserRequest.EXIT);
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

        userRequestsManager.setAccessToken(accessToken);

        System.out.println("Success!");
    }
}
