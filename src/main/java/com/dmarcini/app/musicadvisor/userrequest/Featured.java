package com.dmarcini.app.musicadvisor.userrequest;

public class Featured {
    private final String name;
    private final String url;

    public Featured(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
