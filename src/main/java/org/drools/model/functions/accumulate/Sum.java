package org.drools.model.functions.accumulate;

import org.drools.model.functions.Function1;

import java.io.Serializable;

public class Sum<T, N extends Number> extends AbstractAccumulateFunction<T, Sum.Context<N>, N> {

    private final Function1<T, N> mapper;

    private Sum(Function1<T, N> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Context<N> init() {
        return new Context<N>();
    }

    @Override
    public Context action(Context<N> acc, T obj) {
        return acc.add(mapper.apply(obj));
    }

    @Override
    public Context reverse(Context<N> acc, T obj) {
        return acc.subtract(mapper.apply(obj));
    }

    @Override
    public N result(Context<N> acc) {
        return acc.result();
    }

    public static <T, N extends Number> Sum<T, N> sum(Function1<T, N> mapper) {
        return new Sum<T, N>(mapper);
    }

    public static class Context<N extends Number> implements Serializable {
        private final N total;
        private final Class<N> clazz;

        public Context() {
            this(null, null);
        }

        public Context(N total, Class<N> clazz) {
            this.total = total;
            this.clazz = clazz;
        }

        private Context add(N value) {
            if (value == null) {
                return this;
            }
            if (total == null) {
                return new Context(value, value.getClass());
            }
            return new Context(total.doubleValue() + value.doubleValue(), clazz);
        }

        private Context subtract(N value) {
            if (value == null) {
                return this;
            }
            return new Context(total.doubleValue() - value.doubleValue(), clazz);
        }

        private N result() {
            if (clazz == Integer.class || clazz == int.class) {
                return (N) new Integer(total.intValue());
            }
            if (clazz == Long.class || clazz == long.class) {
                return (N) new Long(total.longValue());
            }
            return total;
        }
    }
}