package org.drools.model;

import org.drools.model.constraints.AbstractConstraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.functions.Predicate1;
import org.drools.model.impl.AccumulateImpl;
import org.drools.model.impl.ExistentialPatternImpl;
import org.drools.model.impl.JavaClassType;
import org.drools.model.impl.JoinFactory;
import org.drools.model.impl.PatternArray;
import org.drools.model.impl.SimplePatternBuilder;
import org.drools.model.impl.SimplePatternImpl;
import org.drools.model.impl.VariableImpl;

public class DSL {

    public static <T> Variable<T> bind(Type<T> type) {
        return new VariableImpl<T>(type);
    }

    public static <T> Type<T> typeOf(Class<T> type) {
        return new JavaClassType<T>(type);
    }

    public static <T> SimplePatternBuilder<T> filter(Variable<T> var) {
        return new SimplePatternImpl<T>(var);
    }

    public static <T> SimplePatternBuilder<T> filter(Type<T> type) {
        return filter(bind(type));
    }

    public static JoinFactory using(Variable... vars) {
        return new JoinFactory(vars);
    }

    public static <A> AbstractConstraint constraint(Variable<A> variable, Predicate1<A> predicate) {
        return new SingleConstraint1<A>(variable, predicate);
    }

    public static AbstractConstraint and(Constraint... constraints) {
        return AbstractConstraint.and(constraints);
    }

    public static AbstractConstraint or(Constraint... constraints) {
        return AbstractConstraint.or(constraints);
    }

    public static View view(Pattern... patterns) {
        return new PatternArray(patterns);
    }

    public static <T> ExistentialPattern not(Pattern<T> pattern) {
        return new ExistentialPatternImpl<T>(ExistentialPattern.ExistentialType.NOT, pattern);
    }

    public static <T> ExistentialPattern<T> exists(Pattern<T> pattern) {
        return new ExistentialPatternImpl<T>(ExistentialPattern.ExistentialType.EXISTS, pattern);
    }

    public static <T> AccumulatePattern<T> accumulate(Pattern<T> pattern, AccumulateFunction<T, ?, ?>... functions) {
        return new AccumulateImpl<T>(pattern, functions);
    }
}
