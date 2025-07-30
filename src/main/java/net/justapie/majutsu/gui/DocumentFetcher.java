package net.justapie.majutsu.gui;

public class DocumentFetcher implements DataFetcher<String, String> {
    @Override
    public String fetch(String key) {
        return "Hi";
    }
}