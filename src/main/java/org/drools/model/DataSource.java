package org.drools.model;

// DataSource is an EntryPoint only supporting inserts (Stream of events)
public interface DataSource<T> {

    void insert(T item);
}
