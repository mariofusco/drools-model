package org.drools.model.constraints;

import org.drools.model.Variable;
import org.drools.model.functions.Predicate1;

public class SingleConstraint1<A> extends Constraint {

    private final Variable<A> variable;
    private final Predicate1<A> predicate;

    public SingleConstraint1(Variable<A> variable, Predicate1<A> predicate) {
        this.variable = variable;
        this.predicate = predicate;
    }
}
