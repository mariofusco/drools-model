package org.drools.model;

import org.drools.model.functions.Block1;
import org.drools.model.impl.DataStoreImpl;

import java.util.Collection;

// A DataSource supporting insert, update & delete
public interface DataStore<T> extends DataSource<T> {

    public DataStore EMPTY = DataStoreImpl.storeOf();

    Collection<T> getObjects();

    <U extends T> void update(U item, Block1<U> f);

    void delete(T item);
}
