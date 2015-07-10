package org.drools.model.flow;

import org.drools.model.Condition;
import org.drools.model.Variable;
import org.drools.model.functions.Predicate1;

import static org.drools.model.functions.LambdaIntrospector.getLambdaFingerprint;

public class Expr1ViewItem<T> extends AbstractExprViewItem<T> {
    private final Predicate1<T> predicate;

    public Expr1ViewItem(Variable<T> var, Predicate1<T> predicate) {
        super(getLambdaFingerprint(predicate, var), var);
        this.predicate = predicate;
    }

    public Expr1ViewItem(String exprId, Variable<T> var, Predicate1<T> predicate) {
        super(exprId, var);
        this.predicate = predicate;
    }

    public Predicate1<T> getPredicate() {
        return predicate;
    }

    @Override
    public Condition.Type getType() {
        return Condition.SingleType.INSTANCE;
    }

}
