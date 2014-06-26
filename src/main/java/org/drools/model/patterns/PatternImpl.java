package org.drools.model.patterns;

import org.drools.model.Condition;
import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Pattern;
import org.drools.model.SingleConstraint;
import org.drools.model.Variable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatternImpl<T> implements Pattern<T> {

    private final Variable<T> variable;
    private final Variable[] inputVariables;
    private final Constraint constraint;
    private final DataSource dataSource;

    PatternImpl(Variable<T> variable, Constraint constraint, DataSource dataSource) {
        this.variable = variable;
        this.constraint = constraint;
        this.dataSource = dataSource;
        this.inputVariables = collectInputVariables();
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

    private Variable[] collectInputVariables() {
        Set<Variable> varSet = new HashSet<Variable>();
        collectInputVariables(constraint, varSet);
        return varSet.toArray(new Variable[varSet.size()]);
    }

    private void collectInputVariables(Constraint constraint, Set<Variable> varSet) {
        if (constraint instanceof SingleConstraint) {
            for (Variable var : ((SingleConstraint)constraint).getVariables()) {
                varSet.add(var);
            }
        } else {
            for (Constraint child : constraint.getChildren()) {
                collectInputVariables(child, varSet);
            }
        }
    }
}
