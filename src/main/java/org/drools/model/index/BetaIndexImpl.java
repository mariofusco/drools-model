package org.drools.model.index;

import org.drools.model.BetaIndex;
import org.drools.model.functions.Function1;

public class BetaIndexImpl<A> extends AbstractIndex<A> implements BetaIndex<A> {

    private final Function1 rightOperandExtractor;

    public BetaIndexImpl(ConstraintType constraintType, Function1<A, ?> leftOperandExtractor, Function1 rightOperandExtractor) {
        super(constraintType, leftOperandExtractor);
        this.rightOperandExtractor = rightOperandExtractor;
    }

    @Override
    public IndexType getIndexType() {
        return IndexType.BETA;
    }

    @Override
    public Function1 getRightOperandExtractor() {
        return rightOperandExtractor;
    }
}
