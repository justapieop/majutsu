package net.justapie.majutsu.gui.cache;

import java.util.ArrayList;
import java.util.HashMap;

import net.justapie.majutsu.gui.fetcher.DocumentFetcher;

public class ContentStore implements Cacheable<ContentStore> {
    private final HashMap<Long, CacheEntry<GenericType>> store = new HashMap<>();
    private long ttlMs;
    private DocumentFetcher fetcher;

    private static final ContentStore INSTANCE = new ContentStore();

    ContentStore() {
    }

    public void setConfiguration(long ttlMs, DocumentFetcher fetcher) {
        this.ttlMs = ttlMs;
        this.fetcher = fetcher;
    }

    @Override
    public ContentStore getInstance() {
        return INSTANCE;
    }

    public void put(long key, GenericType value) {
        store.put(key, new CacheEntry<>(value, ttlMs));
    }

    public GenericType get(long key) {
        CacheEntry<GenericType> entry = store.get(key);

        if (entry == null || entry.isExpired()) {
            GenericType newValue = fetcher.fetch(key);
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
        for (HashMap.Entry<Long, CacheEntry<GenericType>> entry : store.entrySet()) {
            long key = entry.getKey();
            CacheEntry<GenericType> value = entry.getValue();
            if (value.isExpired()) {
                value.refresh(fetcher.fetch(key), ttlMs);
            }
        }
    }

    @Override
    public ArrayList<GenericType> getBorrowed() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<GenericType> getAvailable() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<GenericType> getExpired() {
        return new ArrayList<>();
    }
}
