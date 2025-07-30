package net.justapie.majutsu.gui.cache;

import java.util.Map;
import java.util.HashMap;

import net.justapie.majutsu.gui.fetcher.DocumentFetcher;


public class ContentStore implements Cacheable<ContentStore> {
    private final Map<long, CacheEntry<V>> store = new HashMap<>();
    private long ttlMs;
    private final DocumentFetcher<V> fetcher;

    private static final ContentStore INSTANCE = new ContentStore();

    ContentStore() {
    }

    public void setConfiguration(long ttlMs, DocumentFetcher<V> fetcher) {
        this.ttlMs = ttlMs;
        this.fetcher = fetcher;
    }

    public ContentStore getInstance() {
        return INSTANCE;
    }

    public void put(long key, V value) {
        store.put(key, new CacheEntry<>(value, ttlMs));
    }

    public V get(long key) {
        CacheEntry<V> entry = store.get(key);

        if (entry == null || entry.isExpired()) {
            V newValue = fetcher.fetch(key);
            if (entry == null) {
                put(key, newValue);
            } else {
                entry.refresh(newValue, ttlMs);
            }
            return newValue;
        }

        return entry.getValue();
    }

    public void refreshAll() {
        for (Map.Entry<long, CacheEntry<V>> entry : store.entrySet()) {
            long key = entry.getKey();
            CacheEntry<V> value = entry.getValue();
            if (value.isExpired()) {
                value.refresh(fetcher.fetch(key), ttlMs);
            }
        }
    }
}
