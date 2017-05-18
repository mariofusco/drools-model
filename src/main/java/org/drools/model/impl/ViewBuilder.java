package org.drools.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.drools.model.AccumulateFunction;
import org.drools.model.Condition;
import org.drools.model.Condition.Type;
import org.drools.model.Constraint;
import org.drools.model.DataSourceDefinition;
import org.drools.model.ExistentialPattern;
import org.drools.model.Pattern;
import org.drools.model.Variable;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.patterns.AccumulatePatternImpl;
import org.drools.model.patterns.AndPatterns;
import org.drools.model.patterns.ExistentialPatternImpl;
import org.drools.model.patterns.InvokerMultiValuePatternImpl;
import org.drools.model.patterns.InvokerSingleValuePatternImpl;
import org.drools.model.patterns.OrPatterns;
import org.drools.model.patterns.PatternBuilder;
import org.drools.model.patterns.PatternImpl;
import org.drools.model.view.AccumulateExprViewItem;
import org.drools.model.view.CombinedExprViewItem;
import org.drools.model.view.Expr1ViewItem;
import org.drools.model.view.Expr2ViewItem;
import org.drools.model.view.ExprViewItem;
import org.drools.model.view.InputViewItem;
import org.drools.model.view.SetViewItem;
import org.drools.model.view.ViewItem;
import org.drools.model.view.ViewItemBuilder;

import static java.util.stream.Collectors.toList;
import static org.drools.model.DSL.input;

public class ViewBuilder {

    private ViewBuilder() { }

    public static AndPatterns viewItems2Patterns( ViewItemBuilder[] viewItemBuilders ) {
        List<ViewItem> viewItems = Stream.of( viewItemBuilders ).map( ViewItemBuilder::get ).collect( toList() );
        List<Condition> conditions = viewItems2Conditions( viewItems );
        return new AndPatterns(conditions);
    }
/*
    public static List<Condition> viewItems2ConditionsWithDSL(List<ViewItem> viewItems) {
        Map<Variable<?>, InputViewItem<?>> inputs = new HashMap<>();
        Map<Variable<?>, PatternBuilder.ValidBuilder> builderMap = new HashMap<>();
        List<CombinedExprViewItem> combinedExpressions = new ArrayList<>();
        Set<Variable> variablesFromCombinedExpressions = new HashSet<>();

        for (ViewItem viewItem : viewItems) {
            if (viewItem instanceof CombinedExprViewItem) {
                combinedExpressions.add((CombinedExprViewItem)viewItem);
                variablesFromCombinedExpressions.add(viewItem.getFirstVariable());
                continue;
            }

            Variable var = viewItem.getFirstVariable();
            if (viewItem instanceof InputViewItem) {
                inputs.put(var, (InputViewItem)viewItem);
                continue;
            }

            if (viewItem instanceof SetViewItem) {
                SetViewItem setViewItem = (SetViewItem)viewItem;
                PatternBuilder.ValidBuilder patternBuilder = setViewItem.isMultivalue() ?
                        new PatternBuilder.InvokerMultiValuePatternBuilder( var,
                                                                            DataSourceDefinitionImpl.DEFAULT,
                                                                            setViewItem.getInputVariables(),
                                                                            setViewItem.getInvokedFunction() ) :
                        new PatternBuilder.InvokerSingleValuePatternBuilder( var,
                                                                             DataSourceDefinitionImpl.DEFAULT,
                                                                             setViewItem.getInputVariables(),
                                                                             setViewItem.getInvokedFunction() );
                builderMap.put(var, patternBuilder);
                continue;
            }

            PatternBuilder.ValidBuilder patternBuilder = builderMap.get(var);
            if (patternBuilder == null) {
                patternBuilder = new PatternBuilder().filter(var)
                                                     .from( getDataSourceDefinition( inputs, var ) );
                builderMap.put(var, patternBuilder);
            }

            if (viewItem instanceof ExprViewItem) {
                inputs.computeIfAbsent( viewItem.getFirstVariable(), v -> (InputViewItem) input(v) );
                if ( viewItem instanceof Expr2ViewItem ) {
                    inputs.computeIfAbsent( ( (Expr2ViewItem) viewItem ).getSecondVariable(), v -> (InputViewItem) input(v) );
                }
                builderMap.put(var, expr2PatternBuilder((ExprViewItem)viewItem, patternBuilder));
            }
        }

        for (Variable var : inputs.keySet()) {
            if (!builderMap.containsKey(var) && !variablesFromCombinedExpressions.contains(var)) {
                builderMap.put(var, new PatternBuilder().filter(var)
                                                        .from( getDataSourceDefinition( inputs, var ) ) );
            }
        }

        List<Pattern> patterns = builderMap.values().stream().map(PatternBuilder.ValidBuilder::get).collect( toList() );

        return aggregateConditions(inputs, combinedExpressions, patterns);
    }

    private static PatternBuilder.ValidBuilder expr2PatternBuilder(ExprViewItem viewItem, PatternBuilder.ValidBuilder patternBuilder) {
        if (viewItem instanceof Expr1ViewItem) {
            Expr1ViewItem expr = (Expr1ViewItem)viewItem;
            if (patternBuilder instanceof PatternBuilder.BoundPatternBuilder) {
                patternBuilder = ((PatternBuilder.BoundPatternBuilder) patternBuilder).with(expr.getExprId(), expr.getPredicate());
            } else if (patternBuilder instanceof PatternBuilder.ConstrainedPatternBuilder) {
                patternBuilder = ((PatternBuilder.ConstrainedPatternBuilder) patternBuilder).and(expr.getExprId(), expr.getPredicate());
            }
        } else if (viewItem instanceof Expr2ViewItem) {
            Expr2ViewItem expr = (Expr2ViewItem)viewItem;
            if (patternBuilder instanceof PatternBuilder.BoundPatternBuilder) {
                patternBuilder = ((PatternBuilder.BoundPatternBuilder)patternBuilder).with(expr.getExprId(), expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate());
            } else if (patternBuilder instanceof PatternBuilder.ConstrainedPatternBuilder) {
                patternBuilder = ((PatternBuilder.ConstrainedPatternBuilder)patternBuilder).and(expr.getExprId(), expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate());
            }
        } else if (viewItem instanceof AccumulateExprViewItem) {
            AccumulateExprViewItem acc = (AccumulateExprViewItem)viewItem;
            patternBuilder = new AccumulateBuilder(expr2PatternBuilder(acc.getExpr(), patternBuilder), acc.getFunctions());
        }

        if (viewItem.getExistentialType() == ExistentialPattern.ExistentialType.NOT) {
            patternBuilder = new NotBuilder(patternBuilder);
        } else if (viewItem.getExistentialType() == ExistentialPattern.ExistentialType.EXISTS) {
            patternBuilder = new ExistsBuilder(patternBuilder);
        }
        return patternBuilder;
    }
*/
    private static DataSourceDefinition getDataSourceDefinition( Map<Variable<?>, InputViewItem<?>> inputs, Variable var ) {
        InputViewItem input = inputs.get(var);
        return input != null ? input.getDataSourceDefinition() : DataSourceDefinitionImpl.DEFAULT;
    }

    public static List<Condition> viewItems2Conditions(List<ViewItem> viewItems) {
        Map<Variable<?>, InputViewItem<?>> inputs = new HashMap<>();
        Map<Variable<?>, Pattern> patternsMap = new HashMap<>();
        List<CombinedExprViewItem> combinedExpressions = new ArrayList<>();
        Set<Variable> variablesFromCombinedExpressions = new HashSet<>();

        for (ViewItem viewItem : viewItems) {
            if (viewItem instanceof CombinedExprViewItem) {
                combinedExpressions.add((CombinedExprViewItem)viewItem);
                variablesFromCombinedExpressions.add(viewItem.getFirstVariable());
                continue;
            }

            Variable var = viewItem.getFirstVariable();
            if (viewItem instanceof InputViewItem) {
                inputs.put(var, (InputViewItem)viewItem);
                continue;
            }

            if (viewItem instanceof SetViewItem) {
                SetViewItem setViewItem = (SetViewItem)viewItem;
                Pattern pattern = setViewItem.isMultivalue() ?
                                  new InvokerMultiValuePatternImpl( DataSourceDefinitionImpl.DEFAULT,
                                                                    setViewItem.getInvokedFunction(),
                                                                    var,
                                                                    setViewItem.getInputVariables() ) :
                                  new InvokerSingleValuePatternImpl( DataSourceDefinitionImpl.DEFAULT,
                                                                     setViewItem.getInvokedFunction(),
                                                                     var,
                                                                     setViewItem.getInputVariables() );
                patternsMap.put(var, pattern);
                continue;
            }

            Pattern pattern = patternsMap.get(var);
            if (pattern == null) {
                pattern = new PatternImpl( var, Constraint.EMPTY, getDataSourceDefinition( inputs, var ));
                patternsMap.put(var, pattern);
            }

            if (viewItem instanceof ExprViewItem) {
                inputs.computeIfAbsent( viewItem.getFirstVariable(), v -> (InputViewItem) input(v) );
                if ( viewItem instanceof Expr2ViewItem ) {
                    inputs.computeIfAbsent( ( (Expr2ViewItem) viewItem ).getSecondVariable(), v -> (InputViewItem) input(v) );
                }
                patternsMap.put(var, expr2Pattern((ExprViewItem)viewItem, pattern));
            }
        }

        for (Variable var : inputs.keySet()) {
            if (!patternsMap.containsKey(var) && !variablesFromCombinedExpressions.contains(var)) {
                patternsMap.put(var, new PatternImpl( var, Constraint.EMPTY, getDataSourceDefinition( inputs, var ) ) );
            }
        }

        return aggregateConditions(inputs, combinedExpressions, patternsMap.values());
    }

    private static Pattern expr2Pattern(ExprViewItem viewItem, Pattern pattern) {
        if (viewItem instanceof Expr1ViewItem) {
            Expr1ViewItem expr = (Expr1ViewItem)viewItem;
            ( (PatternImpl) pattern ).addConstraint( new SingleConstraint1( expr.getExprId(), expr.getFirstVariable(), expr.getPredicate() ) );
        } else if (viewItem instanceof Expr2ViewItem) {
            Expr2ViewItem expr = (Expr2ViewItem)viewItem;
            ( (PatternImpl) pattern ).addConstraint( new SingleConstraint2( expr.getExprId(), expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate() ) );
        } else if (viewItem instanceof AccumulateExprViewItem) {
            AccumulateExprViewItem acc = (AccumulateExprViewItem)viewItem;
            pattern = new AccumulatePatternImpl(expr2Pattern(acc.getExpr(), pattern), acc.getFunctions());
        }

        if (viewItem.getExistentialType() == ExistentialPattern.ExistentialType.NOT) {
            pattern = new ExistentialPatternImpl(ExistentialPattern.ExistentialType.NOT, pattern);
        } else if (viewItem.getExistentialType() == ExistentialPattern.ExistentialType.EXISTS) {
            pattern = new ExistentialPatternImpl(ExistentialPattern.ExistentialType.EXISTS, pattern);
        }
        return pattern;
    }

    private static Condition createPatternForCombinedExpression(Map<Variable<?>, InputViewItem<?>> inputs, CombinedExprViewItem combinedExpression) {
        List<CombinedExprViewItem> combinedExpressions = new ArrayList<>();
        List<Pattern> patterns = new ArrayList<>();

        for (ExprViewItem viewItem : combinedExpression.getExpressions()) {
            if (viewItem instanceof CombinedExprViewItem) {
                combinedExpressions.add((CombinedExprViewItem)viewItem);
                continue;
            }
            Variable var = viewItem.getFirstVariable();
            if (viewItem instanceof Expr1ViewItem) {
                Expr1ViewItem expr = (Expr1ViewItem)viewItem;
                Pattern pattern = new PatternBuilder().filter(var)
                                                      .from( getDataSourceDefinition( inputs, var ) )
                                                      .with(expr.getPredicate())
                                                      .get();
                patterns.add(pattern);
            } else if (viewItem instanceof Expr2ViewItem) {
                Expr2ViewItem expr = (Expr2ViewItem)viewItem;
                Pattern pattern = new PatternBuilder().filter(var)
                                                      .from( getDataSourceDefinition( inputs, var ) )
                                                      .with(expr.getFirstVariable(), expr.getSecondVariable(), expr.getPredicate())
                                                      .get();
                patterns.add(pattern);
            }
        }

        List<Condition> conditions = aggregateConditions(inputs, combinedExpressions, patterns);
        if ( combinedExpression.getType() == Type.AND ) {
            return new AndPatterns( conditions );
        } else if ( combinedExpression.getType() == Type.OR ) {
            return new OrPatterns( conditions );
        }
        throw new RuntimeException("Unknown expression type: " + combinedExpression.getType());
    }

    private static List<Condition> aggregateConditions( Map<Variable<?>, InputViewItem<?>> inputs, List<CombinedExprViewItem> combinedExpressions, Collection<Pattern> patterns ) {
        List<Condition> conditions = patterns.stream().sorted( PATTERN_DEPS_COMPARATOR ).collect( toList() );
        for (CombinedExprViewItem combinedExpression : combinedExpressions) {
            conditions.add(createPatternForCombinedExpression(inputs, combinedExpression));
        }
        return conditions;
    }

    private static final PatternDependencyComparator PATTERN_DEPS_COMPARATOR = new PatternDependencyComparator();
    private static class PatternDependencyComparator implements Comparator<Pattern> {
        @Override
        public int compare(Pattern p1, Pattern p2) {
            for (Variable p2Input : p2.getInputVariables()) {
                if (!p2.getPatternVariable().equals(p2Input) && p1.getPatternVariable().equals(p2Input)) {
                    return -1;
                }
            }
            for (Variable p1Input : p1.getInputVariables()) {
                if (!p1.getPatternVariable().equals(p1Input) && p2.getPatternVariable().equals(p1Input)) {
                    return 1;
                }
            }
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
            return new ExistentialPatternImpl(ExistentialPattern.ExistentialType.NOT, builder.get());
        }
    }

    private static class ExistsBuilder implements PatternBuilder.ValidBuilder {
        private final PatternBuilder.ValidBuilder builder;

        private ExistsBuilder(PatternBuilder.ValidBuilder builder) {
            this.builder = builder;
        }

        @Override
        public Pattern get() {
            return new ExistentialPatternImpl(ExistentialPattern.ExistentialType.EXISTS, builder.get());
        }
    }

    private static class AccumulateBuilder implements PatternBuilder.ValidBuilder {
        private final PatternBuilder.ValidBuilder builder;
        private final AccumulateFunction[] functions;

        private AccumulateBuilder(PatternBuilder.ValidBuilder builder, AccumulateFunction[] functions) {
            this.builder = builder;
            this.functions = functions;
        }

        @Override
        public Pattern get() {
            return new AccumulatePatternImpl(builder.get(), functions);
        }
    }
}
