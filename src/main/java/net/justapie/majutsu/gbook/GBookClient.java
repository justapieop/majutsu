package net.justapie.majutsu.gbook;

import net.justapie.majutsu.gbook.fetcher.SearchVolumeFetcher;
import net.justapie.majutsu.gbook.fetcher.VolumeFetcher;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class GBookClient {
    private static final GBookClient INSTANCE = new GBookClient();
    private static final String GBOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    private final HttpClient httpClient;
    private final HttpRequest.Builder httpRequestBuilder;
    private HttpRequest httpRequest;

    private GBookClient() {
        super();
        this.httpClient = HttpClient.newHttpClient();
        this.httpRequestBuilder = HttpRequest.newBuilder();
    }

    public static GBookClient getInstance() {
        return INSTANCE;
    }

    public VolumeFetcher getVolumeById(String id) {
        this.httpRequest = this.httpRequestBuilder.GET().uri(URI.create(GBOOK_BASE_URL + "/" + id)).build();
        return new VolumeFetcher(this.httpClient, this.httpRequest);
    }

    public SearchVolumeFetcher searchVolume(String query) {
        this.httpRequest = this.httpRequestBuilder.GET().uri(URI.create(GBOOK_BASE_URL + "?q=" + query)).build();
        return new SearchVolumeFetcher(this.httpClient, this.httpRequest);
    }
}
