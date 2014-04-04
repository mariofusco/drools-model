package org.drools.model;

public interface JoinModel {

    Expression getExpression(); // non-indexed filter
    Expression[] getLeftIndexes();
    Expression[] getRightIndexes();
}

