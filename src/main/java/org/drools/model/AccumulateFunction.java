package org.drools.model;

public interface AccumulateFunction<T, A, R> {

    A init();

    A action(A acc, T obj);

    A reverse(A acc, T obj);

    R result(A acc);

    Variable<R> getVariable();
}
