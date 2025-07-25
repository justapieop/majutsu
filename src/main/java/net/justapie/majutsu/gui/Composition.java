package net.justapie.majutsu.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class Composition<T extends Parent> {
    private final Scene scene;
    private final T root;

    public Composition(T root) {
        this.root = root;
        this.scene = new Scene(this.root);
    }

    public abstract void setup();

    public Scene getScene() {
        return this.scene;
    }

    public T getLayout() {
        return this.root;
    }
}
