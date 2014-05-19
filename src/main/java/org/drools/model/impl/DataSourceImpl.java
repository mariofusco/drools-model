package org.drools.model.impl;

import org.drools.model.*;

import java.util.*;

public class DataSourceImpl<T> implements DataSource<T> {

    protected final Collection<T> source;

    protected DataSourceImpl(Collection source) {
        this.source = source;
    }

    public static <T> DataSource<T> sourceOf(T... items) {
        DataSource<T> dataSource = new DataSourceImpl(new ArrayList<T>());
        for (T item : items) {
            dataSource.insert(item);
        }
        return dataSource;
    }

    public Collection<T> getObjects() {
        return source;
    }

    public void insert(T item) {
        source.add(item);
    }
}
