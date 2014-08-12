package org.drools.model.impl;

import org.drools.model.DataStore;
import org.drools.model.functions.Block1;

import java.util.ArrayList;
import java.util.Collection;

public class DataStoreImpl<T> implements DataStore<T> {

    private final Collection<T> store;

    private DataStoreImpl(Collection<T> store) {
        this.store = store;
    }

    public static <T> DataStore<T> storeOf(T... items) {
        DataStore<T> dataStore = new DataStoreImpl(new ArrayList<T>());
        for (T item : items) {
            dataStore.insert(item);
        }
        return dataStore;
    }

    @Override
    public Collection<T> getObjects() {
        return store;
    }

    @Override
    public void insert(T item) {
        store.add(item);
    }

    @Override
    public <U extends T> void update(U item, Block1<U> f) {
        store.remove(item);
        f.execute(item);
        store.add(item);
    }

    @Override
    public void delete(T item) {
        store.remove(item);
    }
}
