package org.drools.model.constraints;

import org.drools.model.Variable;
import org.drools.model.functions.Predicate2;

public class SingleConstraint2<A, B> extends AbstractSingleConstraint {

    private final Variable<A> var1;
    private final Variable<B> var2;
    private final Predicate2<A, B> predicate;

    public SingleConstraint2(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
        this.var1 = var1;
        this.var2 = var2;
        this.predicate = predicate;
    }

    @Override
    public Variable[] getVariables() {
        return new Variable[] { var1, var2 };
    }

    @Override
    public Predicate2<A, B> getPredicate() {
        return predicate;
    }
}
