package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.JoinPattern;
import org.drools.model.Variable;

public class JoinPatternImpl<T> extends SinglePatternImpl<T> implements JoinPattern<T> {

    private final Variable[] joinVariables;

    JoinPatternImpl(Variable<T> variable, Variable[] joinVariables, Constraint constraint, DataSource dataSource) {
        super(variable, constraint, dataSource);
        this.joinVariables = joinVariables;
    }

    @Override
    public Variable[] getJoinVariables() {
        return joinVariables;
    }

    @Override
    public Kind getKind() {
        return Kind.JOIN;
    }
}
