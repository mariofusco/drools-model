package org.drools.model.functions;

public final class FunctionUtils {

    private FunctionUtils() { }

    public static <R> FunctionN<R> toFunctionN(final Function0<R> f) {
        return new FunctionN<R>() {
            @Override
            public R apply(Object... objs) {
                return f.apply();
            }
        };
    }

    public static <A, R> FunctionN<R> toFunctionN(final Function1<A, R> f) {
        return new FunctionN<R>() {
            @Override
            public R apply(Object... objs) {
                return f.apply((A)objs[0]);
            }
        };
    }

    public static <A, B, R> FunctionN<R> toFunctionN(final Function2<A, B, R> f) {
        return new FunctionN<R>() {
            @Override
            public R apply(Object... objs) {
                return f.apply((A)objs[0], (B)objs[1]);
            }
        };
    }
}
