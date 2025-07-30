package net.justapie.majutsu.gui;

import java.util.Map;
import java.util.HashMap;

import net.justapie.majutsu.db.repository.document.DocumentRepositoryFactory;
import net.justapie.majutsu.db.schema.book.*;

public class ContentStore<K, V> {
    private final Map<K, CacheEntry<V>> store = new HashMap<>();
    private final long ttlMs;
    private final DataFetcher<K, V> fetcher;

    public ContentStore(long ttlMs, DataFetcher<K, V> fetcher) {
        this.ttlMs = ttlMs;
        this.fetcher = fetcher;
    }

    public void put(K key, V value) {
        store.put(key, new CacheEntry<>(value, ttlMs));
    }

    public V get(K key) {
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
        for (Map.Entry<K, CacheEntry<V>> entry : store.entrySet()) {
            K key = entry.getKey();
            CacheEntry<V> value = entry.getValue();
            if (value.isExpired()) {
                value.refresh(fetcher.fetch(key), ttlMs);
            }
        }
    }
}
