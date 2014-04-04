package org.drools.model;

public interface Tuple {
    Tuple getParent();
    Object get(int index);
    int size();

}
