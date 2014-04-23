package org.drools.model.constraints;

import org.drools.model.Constraint;
import org.drools.model.SingleConstraint;

import java.util.Collection;

public abstract class AbstractSingleConstraint extends AbstractConstraint implements SingleConstraint {
    @Override
    public Collection<? extends Constraint> getChildren() {
        return null;
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }
}
