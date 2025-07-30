package net.justapie.majutsu.gbook.fetcher;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class BaseFetcher<T> extends Thread {
    private final HttpRequest httpRequest;
    private final HttpClient httpClient;
    private HttpResponse<T> httpResponse;

    protected BaseFetcher(HttpClient httpClient, HttpRequest httpRequest) {
        this.httpClient = httpClient;
        this.httpRequest = httpRequest;
    }

    protected HttpClient getHttpClient() {
        return this.httpClient;
    }

    protected HttpRequest getHttpRequest() {
        return this.httpRequest;
    }

    public HttpResponse<T> getHttpResponse() {
        return this.httpResponse;
    }

    public void setHttpResponse(HttpResponse<T> httpResponse) {
        this.httpResponse = httpResponse;
    }

    public abstract Object get();
}
