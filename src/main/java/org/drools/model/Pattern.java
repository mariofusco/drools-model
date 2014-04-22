package org.drools.model;

public interface Pattern {

    enum Type { SIMPLE, JOIN }

    DataSource getDataSource();

    Variable getVariable();

    Constraint getConstraint();

    Type getType();
}
