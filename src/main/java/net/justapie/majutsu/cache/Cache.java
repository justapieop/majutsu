package net.justapie.majutsu.cache;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Cache {
    private static final Cache INSTANCE = new Cache();
    private final HashMap<String, CacheObject> cacheMap;
    private final long ttl;

    private Cache() {
        this.ttl = TimeUnit.MINUTES.toMillis(2);
        this.cacheMap = new HashMap<>();
    }

    public static Cache getInstance() {
        return INSTANCE;
    }

    public <T> void put(String key, T data) {
        this.cacheMap.put(key, new CacheObject<T>(data, this.ttl));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                cacheMap.remove(key);
            }
        }, this.ttl);
    }

    public <T> CacheObject<T> get(String key) {
        CacheObject<T> cacheObject = this.cacheMap.get(key);
        if (Objects.isNull(cacheObject) || new Date().getTime() > (cacheObject.getCreatedAt() + this.ttl)) {
            return null;
        }
        return cacheObject;
    }

    public void remove(String key) {
        this.cacheMap.remove(key);
    }
}
