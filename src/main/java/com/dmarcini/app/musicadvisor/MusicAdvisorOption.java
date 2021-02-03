package com.dmarcini.app.musicadvisor;

public enum MusicAdvisorOption {
    AUTH ("auth"),
    FEATURED_PLAYLISTS("featured"),
    NEW_ALBUMS("new"),
    CATEGORIES ("categories"),
    PLAYLISTS ("playlists"),
    NEXT_PAGE("next"),
    PREV_PAGE("prev"),
    EXIT ("exit");

    private final String text;

    MusicAdvisorOption(String text) {
        this.text = text;
    }

    public static MusicAdvisorOption fromString(String text) {
        for (MusicAdvisorOption musicAdvisorOption : MusicAdvisorOption.values()) {
            if (musicAdvisorOption.text.equalsIgnoreCase(text)) {
                return musicAdvisorOption;
            }
        }

        return null;
    }
}
