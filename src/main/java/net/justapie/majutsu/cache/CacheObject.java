package net.justapie.majutsu.cache;

import java.util.Date;

public class CacheObject<T> {
    private final T data;
    private final long createdAt;

    public CacheObject(T data) {
        this.data = data;
        this.createdAt = new Date().getTime();
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public T getData() {
        return this.data;
    }
}
