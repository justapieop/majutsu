package net.justapie.majutsu.gbook.fetcher;

import net.justapie.majutsu.gbook.handler.SearchVolumeHandler;
import net.justapie.majutsu.gbook.model.SearchItems;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class SearchVolumeFetcher extends BaseFetcher<SearchItems, SearchVolumeHandler> {
    public SearchVolumeFetcher(HttpClient httpClient, HttpRequest httpRequest) {
        super(httpClient, httpRequest, new SearchVolumeHandler());
    }
}
