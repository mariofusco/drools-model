package org.drools.model.flow;

import org.drools.model.AccumulateFunction;
import org.drools.model.Condition;

public class AccumulateExprViewItem<T> extends AbstractExprViewItem<T> {

    private final ExprViewItem<T> expr;
    private final AccumulateFunction<T, ?, ?>[] functions;

    public AccumulateExprViewItem(ExprViewItem<T> expr, AccumulateFunction<T, ?, ?>... functions) {
        super(expr.getFirstVariable());
        this.expr = expr;
        this.functions = functions;
    }

    @Override
    public Condition.Type getType() {
        return Condition.SingleType.INSTANCE;
    }

    public ExprViewItem<T> getExpr() {
        return expr;
    }

    public AccumulateFunction<T, ?, ?>[] getFunctions() {
        return functions;
    }
}
