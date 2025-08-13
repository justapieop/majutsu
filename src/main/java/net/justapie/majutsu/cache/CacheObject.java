package net.justapie.majutsu.cache;

import java.util.Date;

public class CacheObject {
    private final Object data;
    private final long createdAt;
    private final long ttl;

    public CacheObject(Object data, long ttl) {
        this.data = data;
        this.ttl = ttl;
        this.createdAt = new Date().getTime();
    }

    public boolean isExpired() {
        return new Date().getTime() > this.createdAt + this.ttl;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public Object getData() {
        if (this.data != null) {
            return this.data;
        }

        return null;
    }
}
