package net.justapie.majutsu.gbook.fetcher;

import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.cache.CacheObject;
import net.justapie.majutsu.gbook.handler.GetVolumeHandler;
import net.justapie.majutsu.gbook.model.Volume;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Objects;

public class VolumeFetcher extends BaseFetcher<Volume, GetVolumeHandler> {
    public VolumeFetcher(HttpClient httpClient, HttpRequest httpRequest) {
        super(httpClient, httpRequest, new GetVolumeHandler());
    }

    @Override
    public String extractId() {
        String path = this.getHttpRequest().uri().getPath();
        return path.split("/")[path.length() - 1];
    }

    @Override
    public Volume get() {
        CacheObject<Volume> vol = Cache.getInstance().get("volume:" + this.extractId());

        if (!Objects.isNull(vol)) {
            return vol.getData();
        }

        this.start();

        try {
            this.join();

            Volume v = this.getHttpResponse().body();
            Cache.getInstance().put("volume:" + this.extractId(), v);
            return v;
        } catch (InterruptedException e) {
            return null;
        }
    }
}
