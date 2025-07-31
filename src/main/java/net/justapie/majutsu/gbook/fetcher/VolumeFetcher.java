package net.justapie.majutsu.gbook.fetcher;

import net.justapie.majutsu.gbook.handler.GetVolumeHandler;
import net.justapie.majutsu.gbook.model.Volume;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class VolumeFetcher extends BaseFetcher<Volume, GetVolumeHandler> {
    public VolumeFetcher(HttpClient httpClient, HttpRequest httpRequest) {
        super(httpClient, httpRequest, new GetVolumeHandler());
        this.start();
    }
}
