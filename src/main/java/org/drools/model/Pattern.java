package org.drools.model;

public interface Pattern<T> extends Condition {

    DataSource getDataSource();

    Variable<T> getVariable(); // have more than one variable (?)

    Variable[] getInputVariables();

    Constraint getConstraint();
}
