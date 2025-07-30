package net.justapie.majutsu.gbook.fetcher;

import net.justapie.majutsu.gbook.handler.SearchVolumeHandler;
import net.justapie.majutsu.gbook.model.SearchItems;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutionException;

public class SearchVolumeFetcher extends BaseFetcher<SearchItems> {
    public SearchVolumeFetcher(HttpClient httpClient, HttpRequest httpRequest) {
        super(httpClient, httpRequest);
    }

    @Override
    public SearchItems get() {
        return this.getHttpResponse().body();
    }

    @Override
    public void run() {
        try {
            this.setHttpResponse(
                    this.getHttpClient()
                            .sendAsync(
                                    this.getHttpRequest(),
                                    new SearchVolumeHandler()
                            ).get()
            );
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
