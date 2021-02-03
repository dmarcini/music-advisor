package com.dmarcini.app.musicadvisor.userrequest;

public enum UserRequest {
    AUTH ("auth"),
    FEATURED_PLAYLISTS("featured"),
    NEW_ALBUMS("new"),
    CATEGORIES ("categories"),
    PLAYLISTS ("playlists"),
    EXIT ("exit");

    private final String text;

    UserRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static UserRequest fromString(String text) {
        for (UserRequest userRequest : UserRequest.values()) {
            if (userRequest.text.equalsIgnoreCase(text)) {
                return userRequest;
            }
        }

        return null;
    }
}
