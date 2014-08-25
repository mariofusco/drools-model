package org.drools.model;

import java.util.Observer;

public interface DataSource<T> {

    void addObserver(Observer o);
    void deleteObserver(Observer o);
}
