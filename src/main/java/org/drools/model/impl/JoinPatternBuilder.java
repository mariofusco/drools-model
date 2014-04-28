package org.drools.model.impl;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.JoinPattern;
import org.drools.model.Variable;
import org.drools.model.constraints.AbstractConstraint;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;

public interface JoinPatternBuilder<T> extends JoinPattern {

    JoinPatternBuilder<T> from(DataSource dataSource);

    Constrained<T> with(Predicate1<T> predicate);
    <A, B> Constrained<T> with(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate);
    <A> Constrained<T> with(Variable<A> var2, Predicate2<T, A> predicate);
    Constrained<T> with(Constraint constraint);

    public interface Constrained<T> extends JoinPattern {

        Constrained<T> from(DataSource dataSource);

        Constrained<T> and(Predicate1<T> predicate);
        <A, B> Constrained<T> and(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate);
        <A> Constrained<T> and(Variable<A> var2, Predicate2<T, A> predicate);
        Constrained<T> and(AbstractConstraint constraint);

        Constrained<T> or(Predicate1<T> predicate);
        <A, B> Constrained<T> or(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate);
        <A> Constrained<T> or(Variable<A> var2, Predicate2<T, A> predicate);
        Constrained<T> or(AbstractConstraint constraint);
    }
}
