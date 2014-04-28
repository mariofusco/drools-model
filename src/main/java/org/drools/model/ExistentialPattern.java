package org.drools.model;

public interface ExistentialPattern<T> extends JoinPattern<T> {
    enum ExistentialType { NOT, EXISTS }

    ExistentialType getExistentialType();
}
