package org.drools.model.constraints;

import org.drools.model.Constraint;
import org.drools.model.SingleConstraint;

public abstract class AbstractSingleConstraint extends AbstractConstraint implements SingleConstraint {
    @Override
    public Iterable<? extends Constraint> getChildren() {
        return null;
    }

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }
}
