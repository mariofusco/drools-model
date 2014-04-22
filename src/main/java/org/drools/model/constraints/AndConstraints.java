package org.drools.model.constraints;

import org.drools.model.Constraint;

import java.util.ArrayList;
import java.util.List;

public class AndConstraints extends AbstractConstraint {

    private final List<AbstractConstraint> constraints = new ArrayList<AbstractConstraint>();

    AndConstraints(AbstractConstraint... constraints) {
        for (AbstractConstraint constraint : constraints) {
            and(constraint);
        }
    }

    @Override
    public AndConstraints and(AbstractConstraint constraint) {
        constraints.add(constraint);
        return this;
    }

    @Override
    public Iterable<? extends Constraint> getChildren() {
        return constraints;
    }

    @Override
    public Type getType() {
        return Type.AND;
    }
}
