package org.drools.model.impl;

import org.drools.model.SimplePattern;
import org.drools.model.DataSource;
import org.drools.model.Variable;
import org.drools.model.constraints.Constraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.functions.Predicate1;

public class SimplePatternImpl<T> implements SimplePattern<T> {
    private final Variable<T> variable;
    private DataSource dataSource;

    public SimplePatternImpl(Variable<T> variable) {
        this.variable = variable;
    }

    public SimplePatternImpl<T> from(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public SimplePattern.Constrained<T> with(Predicate1<T> predicate) {
        return with(new SingleConstraint1<T>(variable, predicate));
    }

    public SimplePattern.Constrained<T> with(Constraint constraint) {
        return new ConstrainedImpl(variable, constraint);
    }

    public class ConstrainedImpl<T> implements SimplePattern.Constrained<T> {

        private final Variable<T> variable;
        private Constraint constraint;
        private DataSource dataSource;

        public ConstrainedImpl(Variable<T> variable, Constraint constraint) {
            this.variable = variable;
            this.constraint = constraint;
        }

        public ConstrainedImpl<T> from(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public ConstrainedImpl<T> and(Predicate1<T> predicate) {
            return and(new SingleConstraint1<T>(variable, predicate));
        }

        public ConstrainedImpl<T> and(Constraint constraint) {
            constraint = constraint.and(constraint);
            return this;
        }

        public ConstrainedImpl<T> or(Predicate1<T> predicate) {
            return or(new SingleConstraint1<T>(variable, predicate));
        }

        public ConstrainedImpl<T> or(Constraint constraint) {
            constraint = constraint.or(constraint);
            return this;
        }
    }
}
