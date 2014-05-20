package org.drools.model.constraints;

import org.drools.model.Constraint;
import org.drools.model.Index;
import org.drools.model.SingleConstraint;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSingleConstraint extends AbstractConstraint implements SingleConstraint {

    private Index index;

    @Override
    public List<Constraint> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }

    @Override
    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }
}
