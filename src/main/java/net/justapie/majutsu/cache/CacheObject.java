package net.justapie.majutsu.cache;

import java.util.Date;

public class CacheObject implements Cacheable {
    private final long ttl;
    private final Object data;
    private final long createdAt;

    private CacheObject(long ttl, Object data) {
        this.ttl = ttl;
        this.data = data;
        this.createdAt = new Date().getTime();
    }

    public static CacheObject create(long ttl, Object data) {
        return new CacheObject(ttl, data);
    }

    @Override
    public long getTtl() {
        return this.ttl;
    }

    @Override
    public boolean isExpired() {
        return new Date().getTime() > (this.createdAt + this.ttl);
    }

    @Override
    public Object getData() {
        return this.data;
    }
}
