package org.drools.model.flow;

import org.drools.model.Condition;
import org.drools.model.DSL;
import org.drools.model.DataSource;
import org.drools.model.ExistentialPattern;
import org.drools.model.Pattern;
import org.drools.model.Variable;
import org.drools.model.View;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;
import org.drools.model.functions.Provider;
import org.drools.model.patterns.AndPatterns;
import org.drools.model.patterns.OrPatterns;
import org.drools.model.patterns.PatternBuilder;
import org.drools.model.patterns.PatternBuilder.BoundPatternBuilder;
import org.drools.model.patterns.PatternBuilder.ConstrainedPatternBuilder;
import org.drools.model.patterns.PatternBuilder.ValidBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowDSL {

    public static View view(ViewItem... viewItems) {
        List<Condition> conditions = viewItems2Conditions(viewItems);
        return DSL.view(conditions.toArray(new Condition[conditions.size()]));
    }

    public static <T> ViewItem input(Variable<T> var, Provider<DataSource<T>> provider) {
        return new InputViewItem(var, provider);
    }

    public static <T> ExprViewItem expr(Variable<T> var, Predicate1<T> predicate) {
        return new Expr1ViewItem<T>(var, predicate);
    }

    public static <T, U> ExprViewItem expr(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return new Expr2ViewItem<T, U>(var1, var2, predicate);
    }

    public static <T> ExprViewItem not(ExprViewItem expr) {
        return expr.setExistentialType(ExistentialPattern.ExistentialType.NOT);
    }

    public static <T> ExprViewItem not(Variable<T> var, Predicate1<T> predicate) {
        return not(new Expr1ViewItem<T>(var, predicate));
    }

    public static <T, U> ExprViewItem not(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return not(new Expr2ViewItem<T, U>(var1, var2, predicate));
    }

    public static <T> ExprViewItem exists(ExprViewItem expr) {
        return expr.setExistentialType(ExistentialPattern.ExistentialType.EXISTS);
    }

    public static <T> ExprViewItem exists(Variable<T> var, Predicate1<T> predicate) {
        return exists(new Expr1ViewItem<T>(var, predicate));
    }

    public static <T, U> ExprViewItem exists(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return exists(new Expr2ViewItem<T, U>(var1, var2, predicate));
    }

    public static ExprViewItem or(ExprViewItem... expressions) {
        return new CombinedExprViewItem(Condition.OrType.INSTANCE, expressions);
    }

    public static ExprViewItem and(ExprViewItem... expressions) {
        return new CombinedExprViewItem(Condition.AndType.INSTANCE, expressions);
    }

    private static final PatternDependencyComparator PATTERN_DEPS_COMPARATOR = new PatternDependencyComparator();
    private static class PatternDependencyComparator implements Comparator<Pattern> {
        @Override
        public int compare(Pattern p1, Pattern p2) {
            return p1.getInputVariables().length - p2.getInputVariables().length;
        }
    }

    private static List<Condition> viewItems2Conditions(ViewItem[] viewItems) {
        Map<Variable, InputViewItem> inputs = new HashMap<Variable, InputViewItem>();
        List<CombinedExprViewItem> combinedExpressions = new ArrayList<CombinedExprViewItem>();
        Map<Variable, ValidBuilder> builderMap = new HashMap<Variable, ValidBuilder>();

        for (ViewItem viewItem : viewItems) {
            if (viewItem instanceof CombinedExprViewItem) {
                combinedExpressions.add((CombinedExprViewItem)viewItem);
                continue;
            }
            Variable var = viewItem.getFirstVariable();
            if (viewItem instanceof InputViewItem) {
                inputs.put(var, (InputViewItem)viewItem);
                continue;
            }
            ValidBuilder patternBuilder = builderMap.get(var);
            if (patternBuilder == null) {
                patternBuilder = new PatternBuilder().filter(var)
                                                     .from(inputs.get(var).getDataSource());
                builderMap.put(var, patternBuilder);
            }

            if (viewItem instanceof ExprViewItem) {
                if (viewItem instanceof Expr1ViewItem) {
                    Expr1ViewItem expr = (Expr1ViewItem)viewItem;
                    if (patternBuilder instanceof BoundPatternBuilder) {
                        builderMap.put(var, ((BoundPatternBuilder) patternBuilder).with(expr.getPredicate()));
                    } else if (patternBuilder instanceof ConstrainedPatternBuilder) {
                        builderMap.put(var, ((ConstrainedPatternBuilder) patternBuilder).and(expr.getPredicate()));
                    }
                } else if (viewItem instanceof Expr2ViewItem) {
                    Expr2ViewItem expr = (Expr2ViewItem)viewItem;
                    if (patternBuilder instanceof BoundPatternBuilder) {
                        builderMap.put(var, ((BoundPatternBuilder)patternBuilder).with(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate()));
                    } else if (patternBuilder instanceof ConstrainedPatternBuilder) {
                        builderMap.put(var, ((ConstrainedPatternBuilder)patternBuilder).and(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate()));
                    }
                }

                if (((ExprViewItem)viewItem).getExistentialType() == ExistentialPattern.ExistentialType.NOT) {
                    builderMap.put(var, new NotBuilder(builderMap.get(var)));
                } else if (((ExprViewItem)viewItem).getExistentialType() == ExistentialPattern.ExistentialType.EXISTS) {
                    builderMap.put(var, new ExistsBuilder(builderMap.get(var)));
                }
            }
        }

        List<Pattern> patterns = new ArrayList<Pattern>();
        for (ValidBuilder builder : builderMap.values()) {
            patterns.add(builder.get());
        }

        return aggregateConditions(inputs, combinedExpressions, patterns);
    }

    private static class NotBuilder implements ValidBuilder {
        private final ValidBuilder builder;

        private NotBuilder(ValidBuilder builder) {
            this.builder = builder;
        }

        @Override
        public Pattern get() {
            return DSL.not(builder.get());
        }
    }

    private static class ExistsBuilder implements ValidBuilder {
        private final ValidBuilder builder;

        private ExistsBuilder(ValidBuilder builder) {
            this.builder = builder;
        }

        @Override
        public Pattern get() {
            return DSL.exists(builder.get());
        }
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
}
