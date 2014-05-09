package org.drools.model;

public interface SinglePattern<T> extends Pattern {

    enum Kind { SIMPLE, JOIN }

    DataSource getDataSource();

    Variable<T> getVariable();

    Constraint getConstraint();

    Kind getKind();
}
