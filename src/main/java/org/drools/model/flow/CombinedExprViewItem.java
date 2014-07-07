package org.drools.model.flow;

import org.drools.model.Condition;

public class CombinedExprViewItem extends AbstractExprViewItem implements ExprViewItem {

    private final Condition.Type type;
    private final ExprViewItem[] expressions;

    public CombinedExprViewItem(Condition.Type type, ExprViewItem... expressions) {
        super(null);
        this.type = type;
        this.expressions = expressions;
    }

    public ExprViewItem[] getExpressions() {
        return expressions;
    }

    @Override
    public Condition.Type getType() {
        return type;
    }
}
