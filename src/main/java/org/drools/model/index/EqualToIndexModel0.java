package org.drools.model.index;

import org.drools.model.IndexModel;
import org.drools.model.functions.Function0;

public class EqualToIndexModel0 implements IndexModel {
    private final Function0<?> rhs;

    public EqualToIndexModel0(Function0<?> rhs) {
        this.rhs = rhs;
    }
}
