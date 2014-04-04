package org.drools.model.index;

import org.drools.model.IndexModel;
import org.drools.model.expression.Expression0;

public class EqualToIndexModel0 implements IndexModel {
    private final Expression0<?> rhs;

    public EqualToIndexModel0(Expression0<?> rhs) {
        this.rhs = rhs;
    }
}
