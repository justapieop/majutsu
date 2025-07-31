package net.justapie.majutsu.cache;

public interface Cacheable {
    long getTtl();

    boolean isExpired();

    Object getData();
}
