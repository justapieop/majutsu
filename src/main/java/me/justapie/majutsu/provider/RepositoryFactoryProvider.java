package me.justapie.majutsu.provider;

public abstract class RepositoryFactoryProvider<T> {
    protected T repository = null;

    public abstract T create();
}
