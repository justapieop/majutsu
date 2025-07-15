package me.justapie.majutsu.db.provider;

public abstract class RepositoryFactoryProvider<T> {
    protected T repository = null;

    public abstract T create();
}
