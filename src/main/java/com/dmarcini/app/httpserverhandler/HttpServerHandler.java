package com.dmarcini.app.httpserverhandler;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpServerHandler {
    private String query;

    private HttpServer httpServer;
    private HttpRequest httpRequest;

    public HttpServerHandler connectToServer(int port) {
        try {
            httpServer = HttpServer.create();

            httpServer.bind(new InetSocketAddress(port), 0);
            httpServer.createContext("/", exchange -> {
                query = exchange.getRequestURI().getQuery();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void startServer() {
        if (httpServer != null) {
            httpServer.start();
        }
    }

    public void stopServer() {
        if (httpServer != null) {
            httpServer.stop(1);
        }
    }

    public HttpServerHandler waitOnRequest() {
        try {
            while (query == null) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    public String getQuery() {
        return query;
    }

    public HttpServerHandler makeHttpPostRequest(String uri, HttpRequestHeader httpRequestHeader, String requestBody) {
        httpRequest = HttpRequest.newBuilder()
                .header(httpRequestHeader.getName(), httpRequestHeader.getValue())
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return this;
    }

    public HttpServerHandler makeHttpGetRequest(String uri, HttpRequestHeader httpRequestHeader) {
        httpRequest = HttpRequest.newBuilder()
                .header(httpRequestHeader.getName(), httpRequestHeader.getValue())
                .uri(URI.create(uri))
                .GET()
                .build();

        return this;
    }

    public String getHttpResponse() {
        HttpClient httpClient = HttpClient.newBuilder().build();

        if (httpRequest != null) {
            try {
                return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
