package org.drools.model.view;

import org.drools.model.AccumulateFunction;
import org.drools.model.Condition;
import org.drools.model.Condition.Type;

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
        return Type.PATTERN;
    }

    public ExprViewItem<T> getExpr() {
        return expr;
    }

    public AccumulateFunction<T, ?, ?>[] getFunctions() {
        return functions;
    }
}
