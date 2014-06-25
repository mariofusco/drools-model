package org.drools.model;

public interface Pattern<T> extends Condition {

    DataSource getDataSource();

    Variable<T> getPatternVariable();

    Variable[] getBoundVariables();

    Variable[] getInputVariables();

    Constraint getConstraint();
}
