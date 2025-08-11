package net.justapie.majutsu.gui.component;

public abstract class BaseComponent<T> {
    protected T master;

    protected BaseComponent() {
    }

    protected abstract void init();

    public T getMaster() {
        return this.master;
    }
}
