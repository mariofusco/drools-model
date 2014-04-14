package org.drools.model;

import org.drools.model.constraints.Constraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;

public interface JoinPattern<T> extends Pattern {

    JoinPattern<T> from(DataSource dataSource);

    Constrained<T> with(Predicate1<T> predicate);
    <A, B> Constrained<T> with(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate);
    Constrained<T> with(Constraint constraint);

    public interface Constrained<T> extends Pattern {

        Constrained<T> from(DataSource dataSource);

        Constrained<T> and(Predicate1<T> predicate);
        <A, B> Constrained<T> and(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate);
        Constrained<T> and(Constraint constraint);

        Constrained<T> or(Predicate1<T> predicate);
        <A, B> Constrained<T> or(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate);
        Constrained<T> or(Constraint constraint);
    }
}
