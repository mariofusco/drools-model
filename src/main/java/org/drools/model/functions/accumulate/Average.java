package org.drools.model.functions.accumulate;

import org.drools.model.functions.Function1;

import java.io.Serializable;

public class Average<T> extends AbstractAccumulateFunction<T, Average.Context, Double> {

    private final Function1<T, ? extends Number> mapper;

    private Average(Function1<T, ? extends Number> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Context init() {
        return new Context();
    }

    @Override
    public Double result(Context acc) {
        return acc.result();
    }

    @Override
    public Context reverse(Context acc, T obj) {
        return acc.subtract(mapper.apply(obj));
    }

    @Override
    public Context action(Context acc, T obj) {
        return acc.add(mapper.apply(obj));
    }

    public static <T> Average<T> avg(Function1<T, ? extends Number> mapper) {
        return new Average<T>(mapper);
    }

    public static class Context implements Serializable {
        private final double total;
        private final int count;

        private Context() {
            this(0.0, 0);
        }

        private Context(double total, int count) {
            this.total = total;
            this.count = count;
        }

        private Context add(Number value) {
            return new Context(total + value.doubleValue(), count+1);
        }

        private Context subtract(Number value) {
            return new Context(total - value.doubleValue(), count-1);
        }

        private double result() {
            return count == 0 ? 0 : total / count;
        }
    }
}
