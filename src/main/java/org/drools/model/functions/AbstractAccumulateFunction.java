package org.drools.model.functions;

import org.drools.model.AccumulateFunction;
import org.drools.model.Variable;

public abstract class AbstractAccumulateFunction<T, A, R> implements AccumulateFunction<T, A, R> {
    private Variable<R> var;

    @Override
    public Variable<R> getVariable() {
        return var;
    }

    public AbstractAccumulateFunction<T, A, R> as(Variable<R> var) {
        this.var = var;
        return this;
    }
}
