package com.dmarcini.app.musicadvisor.webapi;

import com.google.gson.JsonElement;

public class Category implements Printable {
    private final String id;
    private final String name;

    public Category(JsonElement category) {
        this.id = category.getAsJsonObject().get("id").getAsString();
        this.name = category.getAsJsonObject().get("name").getAsString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void print() {
        System.out.println(name);
    }
}
