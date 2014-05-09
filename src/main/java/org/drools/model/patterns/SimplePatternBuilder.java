package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.SinglePattern;
import org.drools.model.functions.Predicate1;

public interface SimplePatternBuilder<T> extends SinglePattern<T> {

    SimplePatternBuilder<T> from(DataSource dataSource);

    Constrained<T> with(Predicate1<T> predicate);
    Constrained<T> with(Constraint constraint);

    public interface Constrained<T> extends SinglePattern<T> {

        Constrained<T> from(DataSource dataSource);

        Constrained<T> and(Predicate1<T> predicate);
        Constrained<T> and(Constraint constraint);

        Constrained<T> or(Predicate1<T> predicate);
        Constrained<T> or(Constraint constraint);
    }
}
