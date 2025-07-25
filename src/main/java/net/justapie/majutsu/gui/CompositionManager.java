package net.justapie.majutsu.gui;

import javafx.scene.Scene;

public class CompositionManager {
    private static final CompositionManager INSTANCE = new CompositionManager();
    private Composition current;

    public CompositionManager() {
    }

    public static CompositionManager getInstance() {
        return INSTANCE;
    }

    public void setScene(Composition composition) {
        this.current = composition;
        this.current.setup();
    }

    public Scene getCurrentScene() {
        return current.getScene();
    }
}
