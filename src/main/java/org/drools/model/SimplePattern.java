package org.drools.model;

import org.drools.model.constraints.Constraint;
import org.drools.model.functions.Predicate1;

public interface SimplePattern<T> extends Pattern {

    SimplePattern<T> from(DataSource dataSource);

    Constrained<T> with(Predicate1<T> predicate);
    Constrained<T> with(Constraint constraint);

    public interface Constrained<T> extends Pattern {

        Constrained<T> from(DataSource dataSource);

        Constrained<T> and(Predicate1<T> predicate);
        Constrained<T> and(Constraint constraint);

        Constrained<T> or(Predicate1<T> predicate);
        Constrained<T> or(Constraint constraint);
    }
}
