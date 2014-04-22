package org.drools.model.constraints;

import org.drools.model.Constraint;
import org.drools.model.Variable;
import org.drools.model.functions.Predicate1;

public class SingleConstraint1<A> extends AbstractSingleConstraint {

    private final Variable<A> variable;
    private final Predicate1<A> predicate;

    public SingleConstraint1(Variable<A> variable, Predicate1<A> predicate) {
        this.variable = variable;
        this.predicate = predicate;
    }

    @Override
    public Iterable<? extends Constraint> getChildren() {
        return null;
    }

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public Variable[] getVariables() {
        return new Variable[] { variable };
    }

    @Override
    public Predicate1<A> getPredicate() {
        return predicate;
    }
}
