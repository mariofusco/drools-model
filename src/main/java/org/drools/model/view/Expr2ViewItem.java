package org.drools.model.view;

import org.drools.model.Condition;
import org.drools.model.Condition.Type;
import org.drools.model.Variable;
import org.drools.model.functions.Predicate2;

import static org.drools.model.functions.LambdaIntrospector.getLambdaFingerprint;

public class Expr2ViewItem<T, U> extends AbstractExprViewItem<T> {

    private final Variable<U> var2;
    private final Predicate2<T, U> predicate;

    public Expr2ViewItem(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        super(getLambdaFingerprint(predicate, var1, var2), var1);
        this.var2 = var2;
        this.predicate = predicate;
    }

    public Expr2ViewItem(String exprId, Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        super(exprId, var1);
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
        return Type.PATTERN;
    }
}
