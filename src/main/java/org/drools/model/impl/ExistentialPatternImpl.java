package org.drools.model.impl;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.ExistentialPattern;
import org.drools.model.JoinPattern;
import org.drools.model.Pattern;
import org.drools.model.Variable;

public class ExistentialPatternImpl implements ExistentialPattern {

    private final Pattern pattern;
    private final ExistentialType existentialType;

    public ExistentialPatternImpl(ExistentialType existentialType, Pattern pattern) {
        this.existentialType = existentialType;
        this.pattern = pattern;
    }

    @Override
    public Variable[] getJoinVariables() {
        return pattern instanceof JoinPattern ? ((JoinPattern)pattern).getJoinVariables() : null;
    }

    @Override
    public DataSource getDataSource() {
        return pattern.getDataSource();
    }

    @Override
    public Variable getVariable() {
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

    @Override
    public ExistentialType getExistentialType() {
        return existentialType;
    }
}
