package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.ExistentialPattern;
import org.drools.model.JoinPattern;
import org.drools.model.SinglePattern;
import org.drools.model.Variable;

public class ExistentialPatternImpl<T> extends AbstractSinglePattern implements ExistentialPattern<T> {

    private final SinglePattern pattern;
    private final ExistentialType existentialType;

    public ExistentialPatternImpl(ExistentialType existentialType, SinglePattern pattern) {
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
    public Variable<T> getVariable() {
        return pattern.getVariable();
    }

    @Override
    public Constraint getConstraint() {
        return pattern.getConstraint();
    }

    @Override
    public Kind getKind() {
        return pattern.getKind();
    }

    @Override
    public ExistentialType getExistentialType() {
        return existentialType;
    }
}
