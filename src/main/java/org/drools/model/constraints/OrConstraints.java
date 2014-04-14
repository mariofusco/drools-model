package org.drools.model.constraints;

import java.util.ArrayList;
import java.util.List;

public class OrConstraints extends Constraint {

    private final List<Constraint> constraints = new ArrayList<Constraint>();

    OrConstraints(Constraint... constraints) {
        for (Constraint constraint : constraints) {
            or(constraint);
        }
    }

    @Override
    public OrConstraints or(Constraint constraint) {
        constraints.add(constraint);
        return this;
    }
}
