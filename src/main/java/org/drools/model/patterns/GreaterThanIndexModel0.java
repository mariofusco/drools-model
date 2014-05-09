package org.drools.model.patterns;

import org.drools.model.*;
import org.drools.model.functions.*;

public class GreaterThanIndexModel0 implements IndexModel {
    private final Function0<?> rhs;

    public GreaterThanIndexModel0(Function0<?> rhs) {
        this.rhs = rhs;
    }
}
