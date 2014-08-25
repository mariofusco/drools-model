package org.drools.model;

public interface DataStream<T> extends DataSource<T> {

    void insert(T item);
}
