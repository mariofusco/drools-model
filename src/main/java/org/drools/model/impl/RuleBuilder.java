package org.drools.model.impl;

import java.util.HashMap;
import java.util.Map;

import org.drools.model.Condition;
import org.drools.model.Condition.Type;
import org.drools.model.Consequence;
import org.drools.model.DataSourceDefinition;
import org.drools.model.Rule;
import org.drools.model.View;
import org.drools.model.consequences.ConsequenceBuilder;
import org.drools.model.functions.Function1;
import org.drools.model.patterns.CompositePatterns;
import org.drools.model.patterns.PatternBuilder;
import org.drools.model.view.ViewItemBuilder;

import static org.drools.model.impl.ViewBuilder.viewItems2Patterns;

public class RuleBuilder {

    private final String pkg;
    private final String name;

    private String unit;

    private final Map<Rule.Attribute, Object> attributes = new HashMap<Rule.Attribute, Object>();

    public RuleBuilder(String name) {
        this("defaultpkg", name);
    }

    public RuleBuilder(String pkg, String name) {
        this.pkg = pkg;
        this.name = name;
    }

    public RuleBuilder unit(String unit) {
        this.unit = unit;
        return this;
    }

    public RuleBuilder unit(Class<?> unitClass) {
        this.unit = getCanonicalSimpleName(unitClass);
        return this;
    }

    public static String getCanonicalSimpleName(Class<?> c) {
        Class<?> enclosingClass = c.getEnclosingClass();
        return enclosingClass != null ?
               getCanonicalSimpleName(enclosingClass) + "." + c.getSimpleName() :
               c.getSimpleName();
    }

    public RuleBuilder attribute(Rule.Attribute attribute, Object value) {
        attributes.put(attribute, value);
        return this;
    }

    public RuleBuilderWithLHS when(DataSourceDefinition dataSourceDefinition, Function1<PatternBuilder, PatternBuilder.ValidBuilder>... builders) {
        Condition[] patterns = new Condition[builders.length];
        for (int i = 0; i < builders.length; i++) {
            patterns[i] = builders[i].apply(new PatternBuilder().from(dataSourceDefinition)).get();
        }
        return when(patterns);
    }

    public RuleBuilderWithLHS when(Function1<PatternBuilder, PatternBuilder.ValidBuilder>... builders) {
        Condition[] patterns = new Condition[builders.length];
        for (int i = 0; i < builders.length; i++) {
            patterns[i] = builders[i].apply(new PatternBuilder()).get();
        }
        return when(patterns);
    }

    public RuleBuilderWithLHS when(View view) {
        return new RuleBuilderWithLHS(view);
    }

    public RuleBuilderWithLHS when(Condition... patterns) {
        return when(new CompositePatterns( Type.AND, patterns ) );
    }

    public RuleBuilderWithLHS view( ViewItemBuilder... viewItemBuilders ) {
        return when(viewItems2Patterns( viewItemBuilders ));
    }

    public class RuleBuilderWithLHS {
        private final View view;

        public RuleBuilderWithLHS(View view) {
            this.view = view;
        }

        public Rule then(Function1<ConsequenceBuilder, ConsequenceBuilder.ValidBuilder> builder) {
            Consequence consequence = builder.apply(new ConsequenceBuilder()).get();
            return new RuleImpl(pkg, name, unit, view, consequence, attributes);
        }

        public Rule then(ConsequenceBuilder.ValidBuilder builder) {
            return new RuleImpl(pkg, name, unit, view, builder.get(), attributes);
        }
    }
}
