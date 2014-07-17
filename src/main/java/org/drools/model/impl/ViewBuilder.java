package org.drools.model.impl;

import org.drools.model.Condition;
import org.drools.model.DSL;
import org.drools.model.ExistentialPattern;
import org.drools.model.Pattern;
import org.drools.model.Variable;
import org.drools.model.flow.CombinedExprViewItem;
import org.drools.model.flow.Expr1ViewItem;
import org.drools.model.flow.Expr2ViewItem;
import org.drools.model.flow.ExprViewItem;
import org.drools.model.flow.InputViewItem;
import org.drools.model.flow.ViewItem;
import org.drools.model.patterns.AndPatterns;
import org.drools.model.patterns.OrPatterns;
import org.drools.model.patterns.PatternBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewBuilder {

    public static List<Condition> viewItems2Conditions(ViewItem[] viewItems) {
        Map<Variable, InputViewItem> inputs = new HashMap<Variable, InputViewItem>();
        Map<Variable, PatternBuilder.ValidBuilder> builderMap = new HashMap<Variable, PatternBuilder.ValidBuilder>();
        List<CombinedExprViewItem> combinedExpressions = new ArrayList<CombinedExprViewItem>();
        Set<Variable> variablesFromcombinedExpressions = new HashSet<Variable>();

        for (ViewItem viewItem : viewItems) {
            if (viewItem instanceof CombinedExprViewItem) {
                combinedExpressions.add((CombinedExprViewItem)viewItem);
                variablesFromcombinedExpressions.add(viewItem.getFirstVariable());
                continue;
            }
            Variable var = viewItem.getFirstVariable();
            if (viewItem instanceof InputViewItem) {
                inputs.put(var, (InputViewItem)viewItem);
                continue;
            }
            PatternBuilder.ValidBuilder patternBuilder = builderMap.get(var);
            if (patternBuilder == null) {
                patternBuilder = new PatternBuilder().filter(var)
                                                     .from(inputs.get(var).getDataSource());
                builderMap.put(var, patternBuilder);
            }

            if (viewItem instanceof ExprViewItem) {
                if (viewItem instanceof Expr1ViewItem) {
                    Expr1ViewItem expr = (Expr1ViewItem)viewItem;
                    if (patternBuilder instanceof PatternBuilder.BoundPatternBuilder) {
                        builderMap.put(var, ((PatternBuilder.BoundPatternBuilder) patternBuilder).with(expr.getPredicate()));
                    } else if (patternBuilder instanceof PatternBuilder.ConstrainedPatternBuilder) {
                        builderMap.put(var, ((PatternBuilder.ConstrainedPatternBuilder) patternBuilder).and(expr.getPredicate()));
                    }
                } else if (viewItem instanceof Expr2ViewItem) {
                    Expr2ViewItem expr = (Expr2ViewItem)viewItem;
                    if (patternBuilder instanceof PatternBuilder.BoundPatternBuilder) {
                        builderMap.put(var, ((PatternBuilder.BoundPatternBuilder)patternBuilder).with(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate()));
                    } else if (patternBuilder instanceof PatternBuilder.ConstrainedPatternBuilder) {
                        builderMap.put(var, ((PatternBuilder.ConstrainedPatternBuilder)patternBuilder).and(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate()));
                    }
                }

                if (((ExprViewItem)viewItem).getExistentialType() == ExistentialPattern.ExistentialType.NOT) {
                    builderMap.put(var, new NotBuilder(builderMap.get(var)));
                } else if (((ExprViewItem)viewItem).getExistentialType() == ExistentialPattern.ExistentialType.EXISTS) {
                    builderMap.put(var, new ExistsBuilder(builderMap.get(var)));
                }
            }
        }

        for (Variable var : inputs.keySet()) {
            if (!builderMap.containsKey(var) && !variablesFromcombinedExpressions.contains(var)) {
                builderMap.put(var, new PatternBuilder().filter(var)
                                                        .from(inputs.get(var).getDataSource()));
            }
        }

        List<Pattern> patterns = new ArrayList<Pattern>();
        for (PatternBuilder.ValidBuilder builder : builderMap.values()) {
            patterns.add(builder.get());
        }

        return aggregateConditions(inputs, combinedExpressions, patterns);
    }

    private static Condition createPatternForCombinedExpression(Map<Variable, InputViewItem> inputs, CombinedExprViewItem combinedExpression) {
        List<CombinedExprViewItem> combinedExpressions = new ArrayList<CombinedExprViewItem>();
        List<Pattern> patterns = new ArrayList<Pattern>();

        for (ExprViewItem viewItem : combinedExpression.getExpressions()) {
            if (viewItem instanceof CombinedExprViewItem) {
                combinedExpressions.add((CombinedExprViewItem)viewItem);
                continue;
            }
            Variable var = viewItem.getFirstVariable();
            if (viewItem instanceof Expr1ViewItem) {
                Expr1ViewItem expr = (Expr1ViewItem)viewItem;
                Pattern pattern = new PatternBuilder().filter(var)
                                                      .from(inputs.get(var).getDataSource())
                                                      .with(expr.getPredicate())
                                                      .get();
                patterns.add(pattern);
            } else if (viewItem instanceof Expr2ViewItem) {
                Expr2ViewItem expr = (Expr2ViewItem)viewItem;
                Pattern pattern = new PatternBuilder().filter(var)
                                                      .from(inputs.get(var).getDataSource())
                                                      .with(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate())
                                                      .get();
                patterns.add(pattern);
            }
        }

        List<Condition> conditions = aggregateConditions(inputs, combinedExpressions, patterns);
        if (combinedExpression.getType() instanceof Condition.AndType) {
            return new AndPatterns(conditions.toArray(new Condition[conditions.size()]));
        } else if (combinedExpression.getType() instanceof Condition.OrType) {
            return new OrPatterns(conditions.toArray(new Condition[conditions.size()]));
        }
        throw new RuntimeException("Unknown expression type: " + combinedExpression.getType());
    }

    private static List<Condition> aggregateConditions(Map<Variable, InputViewItem> inputs, List<CombinedExprViewItem> combinedExpressions, List<Pattern> patterns) {
        Collections.sort(patterns, PATTERN_DEPS_COMPARATOR);
        List<Condition> conditions = new ArrayList<Condition>();
        conditions.addAll(patterns);
        for (CombinedExprViewItem combinedExpression : combinedExpressions) {
            conditions.add(createPatternForCombinedExpression(inputs, combinedExpression));
        }
        return conditions;
    }

    private static final PatternDependencyComparator PATTERN_DEPS_COMPARATOR = new PatternDependencyComparator();
    private static class PatternDependencyComparator implements Comparator<Pattern> {
        @Override
        public int compare(Pattern p1, Pattern p2) {
            return p1.getInputVariables().length - p2.getInputVariables().length;
        }
    }

    private static class NotBuilder implements PatternBuilder.ValidBuilder {
        private final PatternBuilder.ValidBuilder builder;

        private NotBuilder(PatternBuilder.ValidBuilder builder) {
            this.builder = builder;
        }

        @Override
        public Pattern get() {
            return DSL.not(builder.get());
        }
    }

    private static class ExistsBuilder implements PatternBuilder.ValidBuilder {
        private final PatternBuilder.ValidBuilder builder;

        private ExistsBuilder(PatternBuilder.ValidBuilder builder) {
            this.builder = builder;
        }

        @Override
        public Pattern get() {
            return DSL.exists(builder.get());
        }
    }
}
