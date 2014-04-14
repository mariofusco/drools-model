package org.drools.model.impl;

import org.drools.model.DataSource;
import org.drools.model.JoinPattern;
import org.drools.model.SimplePattern;
import org.drools.model.Variable;
import org.drools.model.constraints.Constraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;

public class JoinPatternImpl<T> implements JoinPattern<T> {

    private final Variable[] lhs;
    private final Variable<T> rhs;
    private Constraint constraints;
    private DataSource dataSource;

    public JoinPatternImpl(Variable[] lhs, Variable<T> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public JoinPatternImpl<T> from(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public ConstrainedImpl<T> with(Predicate1<T> predicate) {
        return with(new SingleConstraint1<T>(rhs, predicate));
    }

    public <A, B> ConstrainedImpl<T> with(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
        return with(new SingleConstraint2<A, B>(var1, var2, predicate));
    }

    public ConstrainedImpl<T> with(Constraint constraint) {
        this.constraints = constraints;
        return new ConstrainedImpl(lhs, rhs, constraint);
    }

    public class ConstrainedImpl<T> implements JoinPattern.Constrained<T> {

        private final Variable[] lhs;
        private final Variable<T> rhs;
        private Constraint constraint;
        private DataSource dataSource;

        public ConstrainedImpl(Variable[] lhs, Variable<T> rhs, Constraint constraint) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.constraint = constraint;
        }

        public ConstrainedImpl<T> from(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public ConstrainedImpl<T> and(Predicate1<T> predicate) {
            return and(new SingleConstraint1<T>(rhs, predicate));
        }

        public <A, B> ConstrainedImpl<T> and(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return and(new SingleConstraint2<A, B>(var1, var2, predicate));
        }

        public ConstrainedImpl<T> and(Constraint constraint) {
            constraint = constraint.and(constraint);
            return this;
        }

        public ConstrainedImpl<T> or(Predicate1<T> predicate) {
            return or(new SingleConstraint1<T>(rhs, predicate));
        }

        public <A, B> ConstrainedImpl<T> or(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return or(new SingleConstraint2<A, B>(var1, var2, predicate));
        }

        public ConstrainedImpl<T> or(Constraint constraint) {
            constraint = constraint.or(constraint);
            return this;
        }
    }
}
