package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.InvokerPattern;
import org.drools.model.Variable;
import org.drools.model.functions.Function0;

public abstract class InvokerPatternImpl<T> extends AbstractSinglePattern implements InvokerPattern<T> {

    private final Function0<DataSource> dataSourceSupplier;
    private final Variable<T> variable;
    private final Variable[] inputVariables;

    InvokerPatternImpl(Function0<DataSource> dataSourceSupplier, Variable<T> boundVariable, Variable... inputVariables) {
        this.dataSourceSupplier = dataSourceSupplier != null ? dataSourceSupplier : Function0.Null.INSTANCE;
        this.variable = boundVariable;
        this.inputVariables = inputVariables;
    }

    @Override
    public Variable<T> getPatternVariable() {
        return variable;
    }

    @Override
    public Variable[] getBoundVariables() {
        return new Variable[] { variable };
    }

    @Override
    public Variable[] getInputVariables() {
        return inputVariables;
    }

    @Override
    public Function0<DataSource> getDataSourceSupplier() {
        return dataSourceSupplier;
    }

    @Override
    public Constraint getConstraint() {
        throw new UnsupportedOperationException("An InvokerPattern doesn't have a Constraint");
    }
}
