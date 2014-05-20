package org.drools.model.index;

import org.drools.model.AlphaIndex;
import org.drools.model.functions.Function1;

public class AlphaIndexImpl<A> extends AbstractIndex<A> implements AlphaIndex<A> {

    private final Object rightValue;

    public AlphaIndexImpl(ConstraintType constraintType, Function1<A, ?> leftOperandExtractor, Object rightValue) {
        super(constraintType, leftOperandExtractor);
        this.rightValue = rightValue;
    }

    @Override
    public Object getRightValue() {
        return rightValue;
    }

    @Override
    public IndexType getIndexType() {
        return IndexType.ALPHA;
    }

}
