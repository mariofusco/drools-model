package org.drools.model.constraints;

import org.drools.model.Constraint;

import java.util.ArrayList;
import java.util.List;

public class OrConstraints extends AbstractConstraint {

    private final List<AbstractConstraint> constraints = new ArrayList<AbstractConstraint>();

    OrConstraints(AbstractConstraint... constraints) {
        for (AbstractConstraint constraint : constraints) {
            or(constraint);
        }
    }

    @Override
    public OrConstraints or(AbstractConstraint constraint) {
        constraints.add(constraint);
        return this;
    }

    @Override
    public Iterable<? extends Constraint> getChildren() {
        return constraints;
    }

    @Override
    public Type getType() {
        return Type.OR;
    }
}
