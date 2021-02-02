package com.dmarcini.app.musicadvisor;

import java.util.Scanner;

public class MusicAdvisor {
    private boolean wasAuth;

    private final Scanner scanner;

    public MusicAdvisor() {
        this.wasAuth = false;
        this.scanner = new Scanner(System.in);
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
        System.out.println("https://accounts.spotify.com/authorize?client_id=9a6ab59d9c8b42a49901c0732372136d" +
                           "&redirect_uri=http://localhost:8080&response_type=code");

        return true;
    }

    private void getFeatured() {

    }

    private void getNew() {

    }

    private void getCategories() {

    }

    private void getPlaylists() {

    }

    private void runIfAuthSuccess(Runnable toRun) {
        if (wasAuth) {
            toRun.run();
        } else {
            System.out.println("Please, provide access for application");
        }
    }
}
