package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSourceDefinition;
import org.drools.model.Pattern;
import org.drools.model.SingleConstraint;
import org.drools.model.Variable;

import java.util.HashSet;
import java.util.Set;

public class PatternImpl<T> extends AbstractSinglePattern implements Pattern<T> {

    private final Variable<T> variable;
    private final Variable[] inputVariables;
    private final Constraint constraint;
    private final DataSourceDefinition dataSourceDefinition;

    PatternImpl(Variable<T> variable, Constraint constraint, DataSourceDefinition dataSourceDefinition) {
        this.variable = variable;
        this.constraint = constraint;
        this.dataSourceDefinition = dataSourceDefinition;
        this.inputVariables = collectInputVariables();
    }

    @Override
    public DataSourceDefinition getDataSourceDefinition() {
        return dataSourceDefinition;
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
