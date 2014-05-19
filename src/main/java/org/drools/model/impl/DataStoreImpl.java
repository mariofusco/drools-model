package org.drools.model.impl;

import org.drools.model.DataStore;
import org.drools.model.functions.Block1;

import java.util.ArrayList;
import java.util.Collection;

public class DataStoreImpl<T> extends DataSourceImpl<T> implements DataStore<T> {

    protected DataStoreImpl(Collection source) {
        super(source);
    }

    public static <T> DataStore<T> storeOf(T... items) {
        DataStore<T> dataStore = new DataStoreImpl(new ArrayList<T>());
        for (T item : items) {
            dataStore.insert(item);
        }
        return dataStore;
    }

    @Override
    public <U extends T> void update(U item, Block1<U> f) {
        source.remove(item);
        f.execute(item);
        source.add(item);
    }

    @Override
    public void delete(T item) {
        source.remove(item);
    }
}
