package org.drools.model.patterns;

import org.drools.model.*;
import org.drools.model.functions.*;

public class GreaterThanIndexModel1 implements IndexModel {
    private final Function1<?,?> lhs;
    private final Function0<?> rhs;

    public GreaterThanIndexModel1(Function1 lhs, Function0<?> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
