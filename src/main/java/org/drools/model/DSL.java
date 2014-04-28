package org.drools.model;

import org.drools.model.constraints.AbstractConstraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.functions.Predicate1;
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

    public static AbstractConstraint and(AbstractConstraint... constraints) {
        return AbstractConstraint.and(constraints);
    }

    public static AbstractConstraint or(AbstractConstraint... constraints) {
        return AbstractConstraint.or(constraints);
    }

    public static View view(Pattern... patterns) {
        return new PatternArray(patterns);
    }

    public static ExistentialPattern not(Pattern pattern) {
        return new ExistentialPatternImpl(ExistentialPattern.ExistentialType.NOT, pattern);
    }

    public static ExistentialPattern exists(Pattern pattern) {
        return new ExistentialPatternImpl(ExistentialPattern.ExistentialType.EXISTS, pattern);
    }
}
