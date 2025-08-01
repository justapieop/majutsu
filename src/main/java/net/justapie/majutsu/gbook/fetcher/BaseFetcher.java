package net.justapie.majutsu.gbook.fetcher;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public abstract class BaseFetcher<T, K extends HttpResponse.BodyHandler<T>> {
    private final HttpRequest httpRequest;
    private final HttpClient httpClient;
    private HttpResponse<T> httpResponse;
    private final K bodyHandler;

    protected BaseFetcher(HttpClient httpClient, HttpRequest httpRequest, K bodyHandler) {
        this.httpClient = httpClient;
        this.httpRequest = httpRequest;
        this.bodyHandler = bodyHandler;
    }

    public HttpRequest getHttpRequest() {
        return this.httpRequest;
    }

    public HttpResponse<T> getHttpResponse() {
        return this.httpResponse;
    }

    public abstract String extractId();

    public abstract T get() throws InterruptedException;

    public void start() {
        try {
            this.httpResponse = this.httpClient.sendAsync(this.httpRequest, this.bodyHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
