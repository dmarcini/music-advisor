package com.dmarcini.app.musicadvisor.userrequest;

public enum Request {
    AUTH ("auth"),
    FEATURED ("featured"),
    NEW ("new"),
    CATEGORIES ("categories"),
    PLAYLISTS ("playlists"),
    EXIT ("exit");

    private String text;

    Request(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Request fromString(String text) {
        for (Request request : Request.values()) {
            if (request.text.equalsIgnoreCase(text)) {
                return request;
            }
        }

        return null;
    }
}
