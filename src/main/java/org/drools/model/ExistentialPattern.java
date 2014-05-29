package org.drools.model;

public interface ExistentialPattern<T> extends Pattern<T> {
    enum ExistentialType { NOT, EXISTS }

    ExistentialType getExistentialType();
}
