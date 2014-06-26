package org.drools.model.flow;

import org.drools.model.Condition;
import org.drools.model.Variable;

public class CombinedExprViewItem implements ExprViewItem {

    private final Condition.Type type;
    private final ExprViewItem[] expressions;

    public CombinedExprViewItem(Condition.Type type, ExprViewItem... expressions) {
        this.type = type;
        this.expressions = expressions;
    }

    @Override
    public Variable getFirstVariable() {
        return null;
    }

    public ExprViewItem[] getExpressions() {
        return expressions;
    }

    @Override
    public Condition.Type getType() {
        return type;
    }
}
