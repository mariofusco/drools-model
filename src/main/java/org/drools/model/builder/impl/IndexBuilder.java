package org.drools.model.builder.impl;

import org.drools.model.Expression;
import org.drools.model.builder.Indexable;
import org.drools.model.expression.Expression0;
import org.drools.model.expression.Expression1;
import org.drools.model.index.EqualToIndexModel0;
import org.drools.model.index.EqualToIndexModel1;

public class IndexBuilder<T extends Indexable> {

    private final T indexable;

    public IndexBuilder(T indexable) {
        this.indexable = indexable;
    }

    public T equalTo(Expression0<?> rhs) {
        indexable.addIndex(new EqualToIndexModel0(rhs));
        return indexable;
    }

    public T equalTo(Expression1<?,?> lhs, Expression0<?> rhs) {
        indexable.addIndex(new EqualToIndexModel1(lhs, rhs));
        return indexable;
    }
/*
    T greaterThan(Expression rhs);
    T greaterThan(Expression lhs, Expression rhs);

    T equalToOrGreaterThan(Expression rhs);
    T equalToOrGreaterThan(Expression lhs, Expression rhs);

    T lessThan(Expression rhs);
    T lessThan(Expression lhs, Expression rhs);

    T equalToOrLessThan(Expression rhs);
    T equalToOrLessThan(Expression lhs, Expression rhs);

    T between(Expression left, Expression right);
    T between(Expression lhs, Expression left, Expression right);

    T betweenInside(Expression left, Expression right);
    T betweenInside(Expression lhs, Expression left, Expression right);

    T betweenLeftInside(Expression left, Expression right);
    T betweenLeftInside(Expression lhs, Expression left, Expression right);

    T betweenRightInside(Expression left, Expression right);
    T betweenRightInside(Expression lhs, Expression left, Expression right);
*/
}
