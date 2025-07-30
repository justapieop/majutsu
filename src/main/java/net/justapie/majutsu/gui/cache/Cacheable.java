package net.justapie.majutsu.gui.cache;

public interface Cacheable<T> {
    T getInstance();
    ArrayList<DUCUMENT_TYPE> getBorrowed();
    ArrayList<DUCUMENT_TYPE> getAvailable();
    ArrayList<DUCUMENT_TYPE> getExpired();
}
