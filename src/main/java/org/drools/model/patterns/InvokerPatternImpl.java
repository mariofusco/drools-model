package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.InvokerPattern;
import org.drools.model.Variable;
import org.drools.model.functions.FunctionN;

public class InvokerPatternImpl<T> extends AbstractSinglePattern implements InvokerPattern<T> {

    private final DataSource dataSource;
    private final FunctionN<T> invokedFunction;
    private final Variable<T> variable;
    private final Variable[] inputVariables;

    InvokerPatternImpl(DataSource dataSource, FunctionN<T> function, Variable<T> boundVariable, Variable... inputVariables) {
        this.dataSource = dataSource;
        this.invokedFunction = function;
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
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Constraint getConstraint() {
        throw new UnsupportedOperationException("An InvokerPattern doesn't have a Constraint");
    }

    @Override
    public FunctionN<T> getInvokedFunction() {
        return invokedFunction;
    }
}
