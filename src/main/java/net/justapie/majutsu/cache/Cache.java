package net.justapie.majutsu.cache;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Cache {
    public static final int DEFAULT_TTL = Math.toIntExact(TimeUnit.MINUTES.toMillis(2));
    public static final int INDEFINITE_TTL = 0;

    private static final Cache INSTANCE = new Cache();
    private final HashMap<String, CacheObject> cacheMap;

    private Cache() {
        this.cacheMap = new HashMap<>();
    }

    public static Cache getInstance() {
        return INSTANCE;
    }

    public <T> void put(String key, T data, int ttl) {
        if (ttl < 0) {
            throw new IllegalArgumentException("ttl must be non-negative");
        }

        this.cacheMap.put(key, new CacheObject(data, ttl));

        if (ttl > 0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    cacheMap.remove(key);
                }
            }, ttl);
        }
    }

    public CacheObject get(String key) {
        CacheObject cacheObject = this.cacheMap.get(key);
        if (Objects.isNull(cacheObject) || cacheObject.isExpired()) {
            return null;
        }
        return cacheObject;
    }

    public void remove(String key) {
        this.cacheMap.remove(key);
    }

    public static int createTtl(TimeUnit unit, int interval) {
        return Math.toIntExact(unit.toMillis(interval));
    }
}
