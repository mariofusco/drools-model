package org.drools.model;

import org.drools.model.functions.Function1;

public interface Index<A> {
    public enum IndexType {
        ALPHA, BETA;
    }

    public enum ConstraintType {
        EQUAL,
        NOT_EQUAL,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        RANGE
    }

    IndexType getIndexType();

    ConstraintType getConstraintType();

    Function1<A, ?> getLeftOperandExtractor();
}
