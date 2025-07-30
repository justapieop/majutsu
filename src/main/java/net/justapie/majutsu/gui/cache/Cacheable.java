package net.justapie.majutsu.gui.cache;

import java.util.ArrayList;

public interface Cacheable<T> {
    T getInstance();
    abstract ArrayList<GenericType> getBorrowed();
    abstract ArrayList<GenericType> getAvailable();
    abstract ArrayList<GenericType> getExpired();
}
