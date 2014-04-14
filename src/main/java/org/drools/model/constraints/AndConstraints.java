package org.drools.model.constraints;

import java.util.ArrayList;
import java.util.List;

public class AndConstraints extends Constraint {

    private final List<Constraint> constraints = new ArrayList<Constraint>();

    AndConstraints(Constraint... constraints) {
        for (Constraint constraint : constraints) {
            and(constraint);
        }
    }

    @Override
    public AndConstraints and(Constraint constraint) {
        constraints.add(constraint);
        return this;
    }
}
