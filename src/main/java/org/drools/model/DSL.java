package org.drools.model;

import org.drools.model.consequences.ConsequenceBuilder;
import org.drools.model.constraints.AbstractConstraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.functions.Function1;
import org.drools.model.functions.Predicate1;
import org.drools.model.impl.JavaClassType;
import org.drools.model.impl.RuleAttributesBuilder;
import org.drools.model.impl.RuleImpl;
import org.drools.model.impl.VariableImpl;
import org.drools.model.patterns.AccumulatePatternImpl;
import org.drools.model.patterns.AndPatterns;
import org.drools.model.patterns.ExistentialPatternImpl;
import org.drools.model.patterns.OrPatterns;
import org.drools.model.patterns.PatternBuilder;

public class DSL {

    public static <T> Variable<T> any(Class<T> type) {
        return bind(typeOf(type));
    }

    public static <T> Variable<T> bind(Type<T> type) {
        return new VariableImpl<T>(type);
    }

    public static <T> Type<T> typeOf(Class<T> type) {
        return new JavaClassType<T>(type);
    }

    public static <T> Pattern<T> pattern(Function1<PatternBuilder, PatternBuilder.ValidBuilder> builder) {
        return builder.apply(new PatternBuilder()).get();
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

    public static Condition and(Condition... patterns) {
        return new AndPatterns(patterns);
    }

    public static Condition or(Condition... patterns) {
        return new OrPatterns(patterns);
    }

    public static View view(DataSource dataSource, Function1<PatternBuilder, PatternBuilder.ValidBuilder>... builders) {
        Condition[] patterns = new Condition[builders.length];
        for (int i = 0; i < builders.length; i++) {
            patterns[i] = builders[i].apply(new PatternBuilder().from(dataSource)).get();
        }
        return view(patterns);
    }

    public static View view(Function1<PatternBuilder, PatternBuilder.ValidBuilder>... builders) {
        Condition[] patterns = new Condition[builders.length];
        for (int i = 0; i < builders.length; i++) {
            patterns[i] = builders[i].apply(new PatternBuilder()).get();
        }
        return view(patterns);
    }

    public static View view(Condition... patterns) {
        return new AndPatterns(patterns);
    }

    public static <T> ExistentialPattern not(Pattern<T> pattern) {
        return new ExistentialPatternImpl<T>(ExistentialPattern.ExistentialType.NOT, pattern);
    }

    public static <T> Function1<PatternBuilder, PatternBuilder.ValidBuilder> not(final Function1<PatternBuilder, PatternBuilder.ValidBuilder<T>> builder) {
        return new Function1<PatternBuilder, PatternBuilder.ValidBuilder>() {
            @Override
            public PatternBuilder.ValidBuilder apply(PatternBuilder patternBuilder) {
                return new PatternBuilder.ValidBuilder() {
                    @Override
                    public Pattern<T> get() {
                        return not(builder.apply(new PatternBuilder()).get());
                    }
                };
            }
        };
    }

    public static <T> ExistentialPattern<T> exists(Pattern<T> pattern) {
        return new ExistentialPatternImpl<T>(ExistentialPattern.ExistentialType.EXISTS, pattern);
    }

    public static <T> Function1<PatternBuilder, PatternBuilder.ValidBuilder> exists(final Function1<PatternBuilder, PatternBuilder.ValidBuilder<T>> builder) {
        return new Function1<PatternBuilder, PatternBuilder.ValidBuilder>() {
            @Override
            public PatternBuilder.ValidBuilder apply(PatternBuilder patternBuilder) {
                return new PatternBuilder.ValidBuilder() {
                    @Override
                    public Pattern<T> get() {
                        return exists(builder.apply(new PatternBuilder()).get());
                    }
                };
            }
        };
    }

    public static <T> AccumulatePattern<T> accumulate(Pattern<T> pattern, AccumulateFunction<T, ?, ?>... functions) {
        return new AccumulatePatternImpl<T>(pattern, functions);
    }

    public static <T> Function1<PatternBuilder, PatternBuilder.ValidBuilder> accumulate(final Function1<PatternBuilder, PatternBuilder.ValidBuilder<T>> builder, final AccumulateFunction<T, ?, ?>... functions) {
        return new Function1<PatternBuilder, PatternBuilder.ValidBuilder>() {
            @Override
            public PatternBuilder.ValidBuilder apply(PatternBuilder patternBuilder) {
                return new PatternBuilder.ValidBuilder() {
                    @Override
                    public Pattern<T> get() {
                        return accumulate(builder.apply(new PatternBuilder()).get(), functions);
                    }
                };
            }
        };
    }

    public static Consequence then(Function1<ConsequenceBuilder, ConsequenceBuilder.ValidBuilder> builder) {
        return builder.apply(new ConsequenceBuilder()).get();
    }

    public static Rule rule(View view, Consequence consequence) {
        return new RuleImpl(view, consequence);
    }

    public static Rule rule(RuleAttributesBuilder attributeBuilder, View view, Consequence consequence) {
        return new RuleImpl(view, consequence, attributeBuilder.get());
    }

    public static RuleAttributesBuilder attributes() {
        return new RuleAttributesBuilder();
    }
}
