package org.drools.model.patterns;

import org.drools.model.Condition;
import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Pattern;
import org.drools.model.Variable;

import java.util.Collections;
import java.util.List;

public class PatternImpl<T> implements Pattern<T> {

    private final Variable<T> variable;
    private final Variable[] inputVariables;
    private final Constraint constraint;
    private final DataSource dataSource;

    PatternImpl(Variable<T> variable, Variable[] inputVariables, Constraint constraint, DataSource dataSource) {
        this.variable = variable;
        this.inputVariables = inputVariables;
        this.constraint = constraint;
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
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
    public Constraint getConstraint() {
        return constraint;
    }

    @Override
    public List<Condition> getSubConditions() {
        return Collections.emptyList();
    }

    @Override
    public Type getType() {
        return SingleType.INSTANCE;
    }
}
