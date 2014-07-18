package org.drools.model.flow;

import org.drools.model.Condition;
import org.drools.model.Variable;
import org.drools.model.functions.Predicate2;

public class Expr2ViewItem<T, U> extends AbstractExprViewItem<T> {

    private final Variable<U> var2;
    private final Predicate2<T, U> predicate;

    public Expr2ViewItem(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        super(var1);
        this.var2 = var2;
        this.predicate = predicate;
    }

    public Predicate2<T, U> getPredicate() {
        return predicate;
    }

    public Variable<U> getSecondVariable() {
        return var2;
    }

    @Override
    public Condition.Type getType() {
        return Condition.SingleType.INSTANCE;
    }
}
