package org.drools.model.index;

import org.drools.model.IndexModel;
import org.drools.model.expression.Expression0;
import org.drools.model.expression.Expression1;

public class EqualToIndexModel1 implements IndexModel {
    private final Expression1<?,?> lhs;
    private final Expression0<?> rhs;

    public EqualToIndexModel1(Expression1 lhs, Expression0<?> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
