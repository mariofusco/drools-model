package org.drools.model.impl;

import org.drools.model.Condition;
import org.drools.model.Consequence;
import org.drools.model.DataSource;
import org.drools.model.Rule;
import org.drools.model.View;
import org.drools.model.consequences.ConsequenceBuilder;
import org.drools.model.flow.ViewItem;
import org.drools.model.functions.Function0;
import org.drools.model.functions.Function1;
import org.drools.model.patterns.AndPatterns;
import org.drools.model.patterns.PatternBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.drools.model.impl.ViewBuilder.viewItems2Conditions;

public class RuleBuilder {

    private final String name;

    private final Map<Rule.Attribute, Object> attributes = new HashMap<Rule.Attribute, Object>();

    public RuleBuilder(String name) {
        this.name = name;
    }

    public RuleBuilder attribute(Rule.Attribute attribute, Object value) {
        attributes.put(attribute, value);
        return this;
    }

    public RuleBuilderWithLHS when(Function0<DataSource> dataSourceSupplier, Function1<PatternBuilder, PatternBuilder.ValidBuilder>... builders) {
        Condition[] patterns = new Condition[builders.length];
        for (int i = 0; i < builders.length; i++) {
            patterns[i] = builders[i].apply(new PatternBuilder().from(dataSourceSupplier)).get();
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

    public RuleBuilderWithLHS when(Condition... patterns) {
        return new RuleBuilderWithLHS(name, attributes, new AndPatterns(patterns));
    }

    public RuleBuilderWithLHS view(ViewItem... viewItems) {
        List<Condition> conditions = viewItems2Conditions(viewItems);
        return when(conditions.toArray(new Condition[conditions.size()]));
    }

    public class RuleBuilderWithLHS {
        private final String name;
        private final Map<Rule.Attribute, Object> attributes;
        private final View view;

        public RuleBuilderWithLHS(String name, Map<Rule.Attribute, Object> attributes, View view) {
            this.name = name;
            this.attributes = attributes;
            this.view = view;
        }

        public Rule then(Function1<ConsequenceBuilder, ConsequenceBuilder.ValidBuilder> builder) {
            Consequence consequence = builder.apply(new ConsequenceBuilder()).get();
            return new RuleImpl(name, view, consequence, attributes);
        }

        public Rule then(ConsequenceBuilder.ValidBuilder builder) {
            return new RuleImpl(name, view, builder.get(), attributes);
        }
    }
}
