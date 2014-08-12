package org.drools.model.impl;

import org.drools.model.DataStream;

import java.util.Observable;

public class DataStreamImpl<T> extends Observable implements DataStream<T> {

    @Override
    public void insert(T item) {
        setChanged();
        notifyObservers(item);
    }
}
