package org.drools.model.impl;

import org.drools.model.AccumulatePattern;
import org.drools.model.AccumulateFunction;
import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Pattern;
import org.drools.model.Variable;

public class AccumulateImpl<T> implements AccumulatePattern<T> {

    private final Pattern<T> pattern;
    private final AccumulateFunction<T, ?, ?>[] functions;

    public AccumulateImpl(Pattern<T> pattern, AccumulateFunction<T, ?, ?>... functions) {
        this.pattern = pattern;
        this.functions = functions;
    }

    @Override
    public AccumulateFunction<T, ?, ?>[] getFunctions() {
        return functions;
    }

    @Override
    public DataSource getDataSource() {
        return pattern.getDataSource();
    }

    @Override
    public Variable<T> getVariable() {
        return pattern.getVariable();
    }

    @Override
    public Constraint getConstraint() {
        return pattern.getConstraint();
    }

    @Override
    public Type getType() {
        return pattern.getType();
    }
}
