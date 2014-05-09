package org.drools.model;

public interface AccumulatePattern<T> extends SinglePattern<T> {

    AccumulateFunction<T, ?, ?>[] getFunctions();
}
