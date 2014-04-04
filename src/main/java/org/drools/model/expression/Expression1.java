package org.drools.model.expression;

import org.drools.model.Expression;

public interface Expression1<T, R> extends Expression {
    R apply(T t);
}
