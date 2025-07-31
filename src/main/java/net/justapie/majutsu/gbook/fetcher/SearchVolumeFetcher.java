package net.justapie.majutsu.gbook.fetcher;

import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.cache.CacheObject;
import net.justapie.majutsu.gbook.handler.SearchVolumeHandler;
import net.justapie.majutsu.gbook.model.SearchItems;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Objects;

public class SearchVolumeFetcher extends BaseFetcher<SearchItems, SearchVolumeHandler> {
    public SearchVolumeFetcher(HttpClient httpClient, HttpRequest httpRequest) {
        super(httpClient, httpRequest, new SearchVolumeHandler());
    }

    @Override
    public String extractId() {
        String queries = this.getHttpRequest().uri().getQuery();
        return queries.split("=")[1];
    }

    @Override
    public SearchItems get() {
        CacheObject<SearchItems> itemsCacheObject = Cache.getInstance().get("search:" + this.extractId());

        if (!Objects.isNull(itemsCacheObject)) {
            return itemsCacheObject.getData();
        }

        this.start();

        try {
            this.join();

            SearchItems items = this.getHttpResponse().body();

            Cache.getInstance().put("search:" + this.extractId(), items);
            return items;
        } catch (InterruptedException e) {
            return null;
        }
    }
}
