package net.justapie.majutsu.db.repository.history;

import net.justapie.majutsu.provider.RepositoryFactoryProvider;

import java.util.Objects;

public class HistoryRepositoryFactory extends RepositoryFactoryProvider<HistoryRepository> {
    private static final HistoryRepositoryFactory INSTANCE = new HistoryRepositoryFactory();

    @Override
    public HistoryRepository create() {
        if (Objects.isNull(this.repository)) {
            this.repository = new HistoryRepository();
        }

        return this.repository;
    }

    public static HistoryRepositoryFactory getInstance() {
        return INSTANCE;
    }
}
