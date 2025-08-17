package net.justapie.majutsu.gbook.handler;

import net.justapie.majutsu.gbook.model.SearchItems;
import net.justapie.majutsu.utils.Utils;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class SearchVolumeHandler implements HttpResponse.BodyHandler<SearchItems> {
    @Override
    public HttpResponse.BodySubscriber<SearchItems> apply(HttpResponse.ResponseInfo responseInfo) {
        HttpResponse.BodySubscriber<String> upstream =
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                (body) -> Utils.getInstance().getGson().fromJson(body, SearchItems.class)
        );
    }
}
