package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.ExistentialPattern;
import org.drools.model.Pattern;
import org.drools.model.Variable;
import org.drools.model.functions.Function0;

public class ExistentialPatternImpl<T> extends AbstractSinglePattern implements ExistentialPattern<T> {

    private final Pattern pattern;
    private final ExistentialType existentialType;

    public ExistentialPatternImpl(ExistentialType existentialType, Pattern pattern) {
        this.existentialType = existentialType;
        this.pattern = pattern;
    }

    @Override
    public Variable[] getInputVariables() {
        return pattern.getInputVariables();
    }

    @Override
    public Function0<DataSource> getDataSourceSupplier() {
        return pattern.getDataSourceSupplier();
    }

    @Override
    public Variable<T> getPatternVariable() {
        return pattern.getPatternVariable();
    }

    @Override
    public Variable[] getBoundVariables() {
        return new Variable[0];
    }

    @Override
    public Constraint getConstraint() {
        return pattern.getConstraint();
    }

    @Override
    public ExistentialType getExistentialType() {
        return existentialType;
    }
}
