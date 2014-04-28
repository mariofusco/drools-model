package org.drools.model.impl;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Variable;
import org.drools.model.constraints.AbstractConstraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.functions.Predicate1;

public class SimplePatternImpl<T> implements SimplePatternBuilder<T> {
    private final Variable<T> variable;
    private DataSource dataSource;

    public SimplePatternImpl(Variable<T> variable) {
        this.variable = variable;
    }

    @Override
    public SimplePatternImpl<T> from(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public SimplePatternBuilder.Constrained<T> with(Predicate1<T> predicate) {
        return with(new SingleConstraint1<T>(variable, predicate));
    }

    @Override
    public SimplePatternBuilder.Constrained<T> with(Constraint constraint) {
        return new ConstrainedImpl(variable, constraint);
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
        return Constraint.True;
    }

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    public static class ConstrainedImpl<T> implements SimplePatternBuilder.Constrained<T> {

        private final Variable<T> variable;
        private Constraint constraint;
        private DataSource dataSource;

        public ConstrainedImpl(Variable<T> variable, Constraint constraint) {
            this.variable = variable;
            this.constraint = constraint;
        }

        @Override
        public ConstrainedImpl<T> from(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        @Override
        public ConstrainedImpl<T> and(Predicate1<T> predicate) {
            return and(new SingleConstraint1<T>(variable, predicate));
        }

        @Override
        public ConstrainedImpl<T> and(Constraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).and(constraint);
            return this;
        }

        @Override
        public ConstrainedImpl<T> or(Predicate1<T> predicate) {
            return or(new SingleConstraint1<T>(variable, predicate));
        }

        @Override
        public ConstrainedImpl<T> or(Constraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).or(constraint);
            return this;
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
        public Type getType() {
            return Type.SIMPLE;
        }
    }
}
