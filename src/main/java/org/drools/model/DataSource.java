package org.drools.model;

import org.drools.model.impl.DataSourceImpl;

import java.util.Collection;

// DataSource is an EntryPoint only supporting inserts (Stream of events)
public interface DataSource<T> {

    public DataSource EMPTY = DataSourceImpl.sourceOf();

    Collection<T> getObjects();

    void insert(T item);
}
