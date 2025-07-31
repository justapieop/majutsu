package net.justapie.majutsu.cache;

import java.util.Date;

public class CacheObject<T> {
    private final long ttl;
    private final T data;
    private final long createdAt;

    public CacheObject(long ttl, T data) {
        this.ttl = ttl;
        this.data = data;
        this.createdAt = new Date().getTime();
    }

    public long getTtl() {
        return this.ttl;
    }

    public boolean isExpired() {
        return new Date().getTime() > (this.createdAt + this.ttl);
    }

    public T getData() {
        return this.data;
    }
}
