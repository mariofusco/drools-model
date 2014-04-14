package org.drools.model.index;

import org.drools.model.*;
import org.drools.model.functions.*;

public class GreaterThanIndexModel1 implements IndexModel {
    private final Expression1<?,?> lhs;
    private final Expression0<?> rhs;

    public GreaterThanIndexModel1(Expression1 lhs, Expression0<?> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
