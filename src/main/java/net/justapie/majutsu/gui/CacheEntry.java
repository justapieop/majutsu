package net.justapie.majutsu.gui;

public class CacheEntry<T> {
    private T value;
    private long expiryTime;

    public CacheEntry(T value, long ttlMs) {
        this.value = value;
        this.expiryTime = System.currentTimeMillis() + ttlMs;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }

    public T getValue() {
        return value;
    }

    public void refresh(T newValue, long ttlMs) {
        this.value = newValue;
        this.expiryTime = System.currentTimeMillis() + ttlMs;
    }
}
