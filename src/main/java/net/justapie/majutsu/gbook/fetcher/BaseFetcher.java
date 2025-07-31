package net.justapie.majutsu.gbook.fetcher;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class BaseFetcher<T, K extends HttpResponse.BodyHandler<T>> extends Thread {
    private final HttpRequest httpRequest;
    private final HttpClient httpClient;
    private HttpResponse<T> httpResponse;
    private final K bodyHandler;

    protected BaseFetcher(HttpClient httpClient, HttpRequest httpRequest, K bodyHandler) {
        this.httpClient = httpClient;
        this.httpRequest = httpRequest;
        this.bodyHandler = bodyHandler;
    }

    protected HttpClient getHttpClient() {
        return this.httpClient;
    }

    protected HttpRequest getHttpRequest() {
        return this.httpRequest;
    }

    public T get() {
        return this.httpResponse.body();
    }

    @Override
    public void run() {
        try {
            this.httpResponse = this.httpClient.sendAsync(this.httpRequest, this.bodyHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
