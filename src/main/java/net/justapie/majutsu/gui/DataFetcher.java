package net.justapie.majutsu.gui;

interface DataFetcher<K, V> {
    V fetch(K key);
}
