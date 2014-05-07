package org.drools.model.functions.accumulate;

import org.drools.model.functions.Function2;

public class Reduce<T, R> extends AbstractAccumulateFunction<T, R, R> {

    private final R zero;
    private final Function2<R, T, R> reducingFunction;

    private Reduce(R zero, Function2<R, T, R> reducingFunction) {
        this.zero = zero;
        this.reducingFunction = reducingFunction;
    }

    @Override
    public R init() {
        return zero;
    }

    @Override
    public R action(R acc, T obj) {
        return reducingFunction.apply(acc, obj);
    }

    @Override
    public R reverse(R acc, T obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public R result(R acc) {
        return acc;
    }

    public static <T, R> Reduce<T, R> reduce(R zero, Function2<R, T, R> reducingFunction) {
        return new Reduce(zero, reducingFunction);
    }
}
