package net.justapie.majutsu.gui;

import net.justapie.majutsu.gui.layouts.Login;

import java.util.HashMap;

public class CompositionStore {
    private static final CompositionStore INSTANCE = new CompositionStore();
    private final HashMap<CompositionType, Composition> compositionMap;

    public CompositionStore() {
        this.compositionMap = new HashMap<>();

        this.compositionMap.put(CompositionType.LOGIN, new Login());
    }

    public static CompositionStore getInstance() {
        return INSTANCE;
    }

    public Composition getComposition(CompositionType type) {
        return this.compositionMap.get(type);
    }
}
