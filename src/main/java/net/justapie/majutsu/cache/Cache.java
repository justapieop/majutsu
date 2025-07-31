package net.justapie.majutsu.cache;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Cache {
    private static final Cache INSTANCE = new Cache();
    private final HashMap<String, CacheObject> cacheMap;
    private long defaultTtl;

    private Cache() {
        this.defaultTtl = TimeUnit.MINUTES.toMillis(2);
        this.cacheMap = new HashMap<>();
    }

    public static Cache getInstance() {
        return INSTANCE;
    }

    public void put(String key, Object data, long ttl) {
        this.cacheMap.put(key, CacheObject.create(ttl, data));
    }

    public void put(String key, Object data) {
        this.put(key, data, this.defaultTtl);
    }

    public CacheObject get(String key) {
        CacheObject cacheObject = this.cacheMap.get(key);
        if (Objects.isNull(cacheObject) || cacheObject.isExpired()) {
            return null;
        }
        return cacheObject;
    }

    public long getDefaultTtl() {
        return this.defaultTtl;
    }

    public void setDefaultTtl(long defaultTtl) {
        this.defaultTtl = defaultTtl;
    }
}
