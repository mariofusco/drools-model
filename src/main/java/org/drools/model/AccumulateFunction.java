package org.drools.model;

import java.io.Serializable;

public interface AccumulateFunction<T, A extends Serializable, R> {

    A init();

    A action(A acc, T obj);

    A reverse(A acc, T obj);

    R result(A acc);

    Variable<R> getVariable();
}
