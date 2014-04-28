package org.drools.model.constraints;

import org.drools.model.Constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AndConstraints extends AbstractConstraint {

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

    @Override
    public Collection<Constraint> getChildren() {
        return constraints;
    }

    @Override
    public Type getType() {
        return Type.AND;
    }
}
