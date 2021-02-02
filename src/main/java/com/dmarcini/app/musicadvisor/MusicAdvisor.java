package com.dmarcini.app.musicadvisor;

import com.dmarcini.app.httpserverhandler.HttpServerHandler;

import java.util.Scanner;

public class MusicAdvisor {
    private final static String CLIENT_ID = "9a6ab59d9c8b42a49901c0732372136d";
    private final static String CLIENT_SECRET = "7e7ced2afefb42b58718e68fae38dbaf";
    private final static String GRANT_TYPE = "authorization_code";
    private final static String REDIRECT_URI = "http://localhost:8080";
    private final static String ACCESS_SERVER_URI = "https://accounts.spotify.com/api/token";

    private final Scanner scanner;

    private final HttpServerHandler httpServerHandler;

    private boolean wasAuth;

    public MusicAdvisor() {
        this.scanner = new Scanner(System.in);
        this.httpServerHandler = new HttpServerHandler();
        this.wasAuth = false;
    }

    public void menu() {
        Request request;

        do {
            request = null;

            while (request == null) {
                request = Request.fromString(scanner.next());
            }

            switch (request) {
                case AUTH -> wasAuth = auth();
                case FEATURED -> runIfAuthSuccess(this::getFeatured);
                case NEW -> runIfAuthSuccess(this::getNew);
                case CATEGORIES -> runIfAuthSuccess(this::getCategories);
                case PLAYLISTS -> runIfAuthSuccess(this::getPlaylists);
            }
        } while (request != Request.EXIT);
    }

    private boolean auth() {
        httpServerHandler.connectToServer(8080).startServer();

        System.out.println("use this link to request the access code:");
        System.out.println("https://accounts.spotify.com/authorize?client_id=" + CLIENT_ID +
                           "&redirect_uri=" + REDIRECT_URI + "&response_type=code");

        httpServerHandler.waitOnRequest().stopServer();

        if (httpServerHandler.getQuery().startsWith("error")) {
            System.out.println("Authorization code not found. Try again.");

            return false;
        }

        String requestBody = "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=" + GRANT_TYPE +
                "&code=" + httpServerHandler.getQuery().substring(5) +
                "&redirect_uri=" + REDIRECT_URI;

        String response = httpServerHandler.makeHttpRequest(ACCESS_SERVER_URI, requestBody).getHttpResponse();

        System.out.println(response);

        return true;
    }
    
    private void runIfAuthSuccess(Runnable toRun) {
        if (wasAuth) {
            toRun.run();
        } else {
            System.out.println("Please, provide access for application");
        }
    }

    private void getFeatured() {

    }

    private void getNew() {

    }

    private void getCategories() {

    }

    private void getPlaylists() {

    }
}
