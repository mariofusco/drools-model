package org.drools.model.impl;

import org.drools.model.DataStream;

import java.util.Observable;
import java.util.Observer;

public class DataStreamImpl<T> extends Observable implements DataStream<T> {

    private final T[] initialItems;

    public DataStreamImpl(T[] initialItems) {
        this.initialItems = initialItems;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        for (T item : initialItems) {
            insert(item);
        }
    }

    @Override
    public void insert(T item) {
        setChanged();
        notifyObservers(item);
    }

    public static <T> DataStream<T> streamOf(T... items) {
        DataStream<T> dataStream = new DataStreamImpl<T>(items);
        return dataStream;
    }
}
