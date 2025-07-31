package net.justapie.majutsu.gbook.handler;

import net.justapie.majutsu.gbook.model.Volume;
import net.justapie.majutsu.utils.Utils;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class GetVolumeHandler implements HttpResponse.BodyHandler<Volume> {
    @Override
    public HttpResponse.BodySubscriber<Volume> apply(HttpResponse.ResponseInfo responseInfo) {
        HttpResponse.BodySubscriber<String> upstream =
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                (body) -> Utils.getInstance().getGson().fromJson(body, Volume.class)

        );
    }
}
