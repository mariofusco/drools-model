package org.drools.model.stream;

import org.drools.model.Condition;
import org.drools.model.DSL;
import org.drools.model.DataSource;
import org.drools.model.Variable;
import org.drools.model.View;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;
import org.drools.model.functions.Provider;
import org.drools.model.patterns.PatternBuilder;
import org.drools.model.patterns.PatternBuilder.BoundPatternBuilder;
import org.drools.model.patterns.PatternBuilder.ConstrainedPatternBuilder;
import org.drools.model.patterns.PatternBuilder.ValidBuilder;

import java.util.HashMap;
import java.util.Map;

public class StreamDSL {

    public static View view(ViewItem... viewItems) {
        Map<Variable, ValidBuilder> builderMap = new HashMap<Variable, ValidBuilder>();
        for (ViewItem viewItem : viewItems) {
            Variable var = viewItem.getFirstVariable();
            ValidBuilder patternBuilder = builderMap.get(var);
            if (patternBuilder == null) {
                patternBuilder = new PatternBuilder().filter(var);
                builderMap.put(var, patternBuilder);
            }
            if (viewItem instanceof InputViewItem) {
                InputViewItem input = (InputViewItem)viewItem;
                if (patternBuilder instanceof BoundPatternBuilder) {
                    ((BoundPatternBuilder)patternBuilder).from(input.getDataSource());
                } else if (patternBuilder instanceof ConstrainedPatternBuilder) {
                    ((ConstrainedPatternBuilder)patternBuilder).from(input.getDataSource());
                }
            } else if (viewItem instanceof Expr1ViewItem) {
                Expr1ViewItem expr = (Expr1ViewItem)viewItem;
                if (patternBuilder instanceof BoundPatternBuilder) {
                    builderMap.put(var, ((BoundPatternBuilder)patternBuilder).with(expr.getPredicate()));
                } else if (patternBuilder instanceof ConstrainedPatternBuilder) {
                    builderMap.put(var, ((ConstrainedPatternBuilder)patternBuilder).and(expr.getPredicate()));
                }
            } else if (viewItem instanceof Expr2ViewItem) {
                Expr2ViewItem expr = (Expr2ViewItem)viewItem;
                if (patternBuilder instanceof BoundPatternBuilder) {
                    builderMap.put(var, ((BoundPatternBuilder)patternBuilder).with(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate()));
                } else if (patternBuilder instanceof ConstrainedPatternBuilder) {
                    builderMap.put(var, ((ConstrainedPatternBuilder)patternBuilder).and(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate()));
                }
            }
        }

        Condition[] patterns = new Condition[builderMap.size()];
        int i = 0;
        for (ValidBuilder builder : builderMap.values()) {
            patterns[i++] = builder.get();
        }
        return DSL.view(patterns);
    }

    public static <T> ViewItem input(Variable<T> var, Provider<DataSource<T>> provider) {
        return new InputViewItem(var, provider);
    }

    public static <T> ViewItem expr(Variable<T> var, Predicate1<T> predicate) {
        return new Expr1ViewItem<>(var, predicate);
    }

    public static <T, U> ViewItem expr(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return new Expr2ViewItem<>(var1, var2, predicate);
    }
}
