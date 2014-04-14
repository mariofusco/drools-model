package org.drools.model;

import org.drools.model.constraints.Constraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.functions.Predicate1;
import org.drools.model.impl.JavaClassType;
import org.drools.model.impl.JoinFactory;
import org.drools.model.impl.PatternArray;
import org.drools.model.impl.SimplePatternImpl;
import org.drools.model.impl.VariableImpl;

public class DSL {

    public static <T> Variable<T> bind(Type<T> type) {
        return new VariableImpl<T>(type);
    }

    public static <T> Type<T> typeOf(Class<T> type) {
        return new JavaClassType<T>(type);
    }

    public static <T> SimplePattern<T> filter(Variable<T> var) {
        return new SimplePatternImpl<T>(var);
    }

    public static <T> SimplePattern<T> filter(Type<T> type) {
        return filter(bind(type));
    }

    public static JoinFactory using(Variable... vars) {
        return new JoinFactory(vars);
    }

    public static <A> Constraint constraint(Variable<A> variable, Predicate1<A> predicate) {
        return new SingleConstraint1<A>(variable, predicate);
    }

    public static Constraint and(Constraint... constraints) {
        return Constraint.and(constraints);
    }

    public static Constraint or(Constraint... constraints) {
        return Constraint.or(constraints);
    }

    public static Pattern patterns(Pattern... patterns) {
        return new PatternArray(patterns);
    }
}
