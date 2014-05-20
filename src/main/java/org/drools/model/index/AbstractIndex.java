package org.drools.model.index;

import org.drools.model.Index;
import org.drools.model.functions.Function1;

public abstract class AbstractIndex<A> implements Index<A> {

    private final ConstraintType constraintType;
    private final Function1<A, ?> leftOperandExtractor;

    protected AbstractIndex(ConstraintType constraintType, Function1<A, ?> leftOperandExtractor) {
        this.constraintType = constraintType;
        this.leftOperandExtractor = leftOperandExtractor;
    }

    @Override
    public ConstraintType getConstraintType() {
        return constraintType;
    }

    @Override
    public Function1<A, ?> getLeftOperandExtractor() {
        return leftOperandExtractor;
    }
}
