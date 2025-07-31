package net.justapie.majutsu.gbook.fetcher;

import net.justapie.majutsu.gbook.handler.GetVolumeHandler;
import net.justapie.majutsu.gbook.model.Volume;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutionException;

public class VolumeFetcher extends BaseFetcher<Volume> {
    public VolumeFetcher(HttpClient httpClient, HttpRequest httpRequest) {
        super(httpClient, httpRequest);
        this.start();
    }

    @Override
    public Volume get() {
        return this.getHttpResponse().body();
    }

    @Override
    public void run() {
        try {
            this.setHttpResponse(
                    this.getHttpClient()
                            .sendAsync(
                                    this.getHttpRequest(),
                                    new GetVolumeHandler()
                            ).get()
            );
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
