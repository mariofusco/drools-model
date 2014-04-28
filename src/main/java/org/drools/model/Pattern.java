package org.drools.model;

public interface Pattern<T> {

    enum Type { SIMPLE, JOIN }

    DataSource getDataSource();

    Variable<T> getVariable();

    Constraint getConstraint();

    Type getType();
}
