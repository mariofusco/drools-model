package org.drools.model.index;

import org.drools.model.IndexModel;
import org.drools.model.functions.Function0;
import org.drools.model.functions.Function1;

public class EqualToIndexModel1 implements IndexModel {
    private final Function1<?,?> lhs;
    private final Function0<?> rhs;

    public EqualToIndexModel1(Function1 lhs, Function0<?> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
