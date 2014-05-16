package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Pattern;
import org.drools.model.SinglePattern;
import org.drools.model.Variable;

import java.util.Collections;
import java.util.List;

public class SinglePatternImpl<T> implements SinglePattern<T> {

    private final Variable<T> variable;
    private final Constraint constraint;
    private final DataSource dataSource;

    SinglePatternImpl(Variable<T> variable, Constraint constraint, DataSource dataSource) {
        this.variable = variable;
        this.constraint = constraint;
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Variable<T> getVariable() {
        return variable;
    }

    @Override
    public Constraint getConstraint() {
        return constraint;
    }

    @Override
    public Kind getKind() {
        return Kind.SIMPLE;
    }

    @Override
    public List<Pattern> getPatterns() {
        return Collections.emptyList();
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }
}
