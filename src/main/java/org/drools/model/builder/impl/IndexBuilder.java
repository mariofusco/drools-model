package org.drools.model.builder.impl;

import org.drools.model.builder.Indexable;
import org.drools.model.expression.Expression0;
import org.drools.model.expression.Expression1;
import org.drools.model.index.*;

public class IndexBuilder<T, I extends Indexable> {

    private final I indexable;

    public IndexBuilder(I indexable) {
        this.indexable = indexable;
    }

    public I equalTo(Expression0<?> rhs) {
        indexable.addIndex(new EqualToIndexModel0(rhs));
        return indexable;
    }

    public <R> I equalTo(Expression1<T,R> lhs, Expression0<R> rhs) {
        indexable.addIndex(new EqualToIndexModel1(lhs, rhs));
        return indexable;
    }

    public I greaterThan(Expression0<?> rhs) {
        indexable.addIndex(new GreaterThanIndexModel0(rhs));
        return indexable;
    }

    public <R> I greaterThan(Expression1<T,R> lhs, Expression0<R> rhs) {
        indexable.addIndex(new GreaterThanIndexModel1(lhs, rhs));
        return indexable;
    }
/*
    I equalToOrGreaterThan(Expression rhs);
    I equalToOrGreaterThan(Expression lhs, Expression rhs);

    I lessThan(Expression rhs);
    I lessThan(Expression lhs, Expression rhs);

    I equalToOrLessThan(Expression rhs);
    I equalToOrLessThan(Expression lhs, Expression rhs);

    I between(Expression left, Expression right);
    I between(Expression lhs, Expression left, Expression right);

    I betweenInside(Expression left, Expression right);
    I betweenInside(Expression lhs, Expression left, Expression right);

    I betweenLeftInside(Expression left, Expression right);
    I betweenLeftInside(Expression lhs, Expression left, Expression right);

    I betweenRightInside(Expression left, Expression right);
    I betweenRightInside(Expression lhs, Expression left, Expression right);
*/
}
