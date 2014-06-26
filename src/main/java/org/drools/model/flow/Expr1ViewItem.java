package org.drools.model.flow;

import org.drools.model.Variable;
import org.drools.model.functions.Predicate1;

public class Expr1ViewItem<T> implements ViewItem {
    private final Variable<T> var;
    private final Predicate1<T> predicate;

    public Expr1ViewItem(Variable<T> var, Predicate1<T> predicate) {
        this.var = var;
        this.predicate = predicate;
    }

    @Override
    public Variable getFirstVariable() {
        return var;
    }

    public Predicate1<T> getPredicate() {
        return predicate;
    }
}
