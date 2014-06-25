package org.drools.model.stream;

import org.drools.model.Variable;
import org.drools.model.functions.Predicate2;

public class Expr2ViewItem<T, U> implements ViewItem {

    private final Variable<T> var1;
    private final Variable<U> var2;
    private final Predicate2<T, U> predicate;

    public Expr2ViewItem(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        this.var1 = var1;
        this.var2 = var2;
        this.predicate = predicate;
    }

    @Override
    public Variable getFirstVariable() {
        return var1;
    }

    public Predicate2<T, U> getPredicate() {
        return predicate;
    }

    public Variable<U> getSecondVariable() {
        return var2;
    }
}
