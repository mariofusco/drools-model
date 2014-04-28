package org.drools.model.impl;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Pattern;
import org.drools.model.functions.Predicate1;

public interface SimplePatternBuilder<T> extends Pattern<T> {

    SimplePatternBuilder<T> from(DataSource dataSource);

    Constrained<T> with(Predicate1<T> predicate);
    Constrained<T> with(Constraint constraint);

    public interface Constrained<T> extends Pattern<T> {

        Constrained<T> from(DataSource dataSource);

        Constrained<T> and(Predicate1<T> predicate);
        Constrained<T> and(Constraint constraint);

        Constrained<T> or(Predicate1<T> predicate);
        Constrained<T> or(Constraint constraint);
    }
}
