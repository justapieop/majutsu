package net.justapie.majutsu.gui.fetcher;

interface DataFetchable<V> {
    V fetch(long key);
}
