package org.drools.model;

public interface Variable<T> {

    Type<T> getType();

    String getName();

    default boolean isFact() {
        return true;
    }
}
