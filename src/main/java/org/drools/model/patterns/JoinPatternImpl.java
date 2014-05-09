package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Variable;
import org.drools.model.constraints.AbstractConstraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;

public class JoinPatternImpl<T> extends AbstractSinglePattern implements JoinPatternBuilder<T> {

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

    @Override
    public ConstrainedImpl<T> with(Predicate1<T> predicate) {
        return with(new SingleConstraint1<T>(rhs, predicate));
    }

    @Override
    public <A, B> ConstrainedImpl<T> with(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
        return with(new SingleConstraint2<A, B>(var1, var2, predicate));
    }

    @Override
    public <A> ConstrainedImpl<T> with(Variable<A> var2, Predicate2<T, A> predicate) {
        return with(new SingleConstraint2<T, A>(rhs, var2, predicate));
    }

    @Override
    public ConstrainedImpl<T> with(Constraint constraint) {
        this.constraints = constraints;
        return new ConstrainedImpl(lhs, rhs, constraint);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Variable<T> getVariable() {
        return rhs;
    }

    @Override
    public Constraint getConstraint() {
        return Constraint.True;
    }

    @Override
    public Variable[] getJoinVariables() {
        return lhs;
    }

    @Override
    public Kind getKind() {
        return Kind.JOIN;
    }

    public static class ConstrainedImpl<T> extends AbstractSinglePattern implements JoinPatternBuilder.Constrained<T> {

        private final Variable[] lhs;
        private final Variable<T> rhs;
        private Constraint constraint;
        private DataSource dataSource;

        public ConstrainedImpl(Variable[] lhs, Variable<T> rhs, Constraint constraint) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.constraint = constraint;
        }

        @Override
        public ConstrainedImpl<T> from(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        @Override
        public ConstrainedImpl<T> and(Predicate1<T> predicate) {
            return and(new SingleConstraint1<T>(rhs, predicate));
        }

        @Override
        public <A, B> ConstrainedImpl<T> and(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return and(new SingleConstraint2<A, B>(var1, var2, predicate));
        }

        @Override
        public <A> ConstrainedImpl<T> and(Variable<A> var1, Predicate2<T, A> predicate) {
            return and(new SingleConstraint2<T, A>(rhs, var1, predicate));
        }

        @Override
        public ConstrainedImpl<T> and(AbstractConstraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).and(constraint);
            return this;
        }

        @Override
        public ConstrainedImpl<T> or(Predicate1<T> predicate) {
            return or(new SingleConstraint1<T>(rhs, predicate));
        }

        @Override
        public <A, B> ConstrainedImpl<T> or(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return or(new SingleConstraint2<A, B>(var1, var2, predicate));
        }

        @Override
        public <A> ConstrainedImpl<T> or(Variable<A> var1, Predicate2<T, A> predicate) {
            return or(new SingleConstraint2<T, A>(rhs, var1, predicate));
        }


        @Override
        public ConstrainedImpl<T> or(AbstractConstraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).or(constraint);
            return this;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        @Override
        public Variable<T> getVariable() {
            return rhs;
        }

        @Override
        public Constraint getConstraint() {
            return constraint;
        }

        @Override
        public Variable[] getJoinVariables() {
            return lhs;
        }

        @Override
        public Kind getKind() {
            return Kind.JOIN;
        }
    }
}
