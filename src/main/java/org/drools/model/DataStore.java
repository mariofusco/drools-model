package org.drools.model;

import org.drools.model.functions.Block1;

// A DataSource supporting insert, update & delete
public interface DataStore<T> extends DataSource<T> {

    <U extends T> void update(U item, Block1<U> f);

    void delete(T item);
}
