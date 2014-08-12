package org.drools.model;

import org.drools.model.functions.Function0;

public interface Pattern<T> extends Condition {

    Function0<DataSource> getDataSourceSupplier();

    Variable<T> getPatternVariable();

    Variable[] getBoundVariables();

    Variable[] getInputVariables();

    Constraint getConstraint();
}
