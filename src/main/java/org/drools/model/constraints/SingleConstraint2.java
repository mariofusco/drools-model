package org.drools.model.constraints;

import org.drools.model.Variable;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;

public class SingleConstraint2<A, B> extends Constraint {

    private final Variable<A> var1;
    private final Variable<B> var2;
    private final Predicate2<A, B> predicate;

    public SingleConstraint2(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
        this.var1 = var1;
        this.var2 = var2;
        this.predicate = predicate;
    }
}
