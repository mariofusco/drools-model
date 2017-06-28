package org.drools.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.drools.model.Condition;
import org.drools.model.Condition.Type;
import org.drools.model.Constraint;
import org.drools.model.DataSourceDefinition;
import org.drools.model.Pattern;
import org.drools.model.Variable;
import org.drools.model.View;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.patterns.AccumulatePatternImpl;
import org.drools.model.patterns.CompositePatterns;
import org.drools.model.patterns.InvokerMultiValuePatternImpl;
import org.drools.model.patterns.InvokerSingleValuePatternImpl;
import org.drools.model.patterns.PatternImpl;
import org.drools.model.view.AccumulateExprViewItem;
import org.drools.model.view.CombinedExprViewItem;
import org.drools.model.view.Expr1ViewItemImpl;
import org.drools.model.view.Expr2ViewItemImpl;
import org.drools.model.view.ExprViewItem;
import org.drools.model.view.InputViewItem;
import org.drools.model.view.SetViewItem;
import org.drools.model.view.ViewItem;
import org.drools.model.view.ViewItemBuilder;

import static java.util.stream.Collectors.toList;
import static org.drools.model.DSL.input;

public class ViewBuilder {

    private ViewBuilder() { }

    public static View viewItems2Patterns( ViewItemBuilder[] viewItemBuilders ) {
        List<ViewItem> viewItems = Stream.of( viewItemBuilders ).map( ViewItemBuilder::get ).collect( toList() );
        return (View) viewItems2Condition( viewItems, new HashMap<>(), new HashSet<>(), Type.AND, true );
    }

    public static Condition viewItems2Condition(List<ViewItem> viewItems, Map<Variable<?>, InputViewItem<?>> inputs,
                                                Set<Variable<?>> usedVars, Condition.Type type, boolean topLevel) {
        List<Condition> conditions = new ArrayList<>();
        Map<Variable<?>, Pattern> patternsMap = new HashMap<>();
        for (ViewItem viewItem : viewItems) {
            if ( viewItem instanceof CombinedExprViewItem ) {
                CombinedExprViewItem combined = (CombinedExprViewItem) viewItem;
                conditions.add( viewItems2Condition( Arrays.asList( combined.getExpressions() ), inputs, usedVars, combined.getType(), false ) );
                continue;
            }

            Variable<?> var = viewItem.getFirstVariable();
            if ( viewItem instanceof InputViewItem ) {
                inputs.put( var, (InputViewItem) viewItem );
                continue;
            }

            if ( viewItem instanceof SetViewItem ) {
                SetViewItem setViewItem = (SetViewItem) viewItem;
                Pattern pattern = setViewItem.isMultivalue() ?
                                  new InvokerMultiValuePatternImpl( DataSourceDefinitionImpl.DEFAULT,
                                                                    setViewItem.getInvokedFunction(),
                                                                    var,
                                                                    setViewItem.getInputVariables() ) :
                                  new InvokerSingleValuePatternImpl( DataSourceDefinitionImpl.DEFAULT,
                                                                     setViewItem.getInvokedFunction(),
                                                                     var,
                                                                     setViewItem.getInputVariables() );
                patternsMap.put( var, pattern );
                conditions.add( pattern );
                continue;
            }

            usedVars.add(var);
            Pattern pattern;
            if ( type == Type.AND ) {
                pattern = patternsMap.get( var );
                if ( pattern == null ) {
                    pattern = new PatternImpl( var, Constraint.EMPTY, getDataSourceDefinition( inputs, var ) );
                    patternsMap.put( var, pattern );
                    conditions.add( pattern );
                }
            } else {
                pattern = new PatternImpl( var, Constraint.EMPTY, getDataSourceDefinition( inputs, var ) );
                conditions.add( pattern );
            }

            if (viewItem instanceof ExprViewItem ) {
                inputs.putIfAbsent( var, (InputViewItem) input(var) );
                if ( viewItem instanceof Expr2ViewItemImpl ) {
                    Variable<?> var2 = ( (Expr2ViewItemImpl) viewItem ).getSecondVariable();
                    inputs.putIfAbsent(var2, (InputViewItem) input( var2 ));
                }

                Pattern modifiedPattern = expr2Pattern( (ExprViewItem) viewItem, pattern );
                conditions.set( conditions.indexOf( pattern ), modifiedPattern );
                if (type == Type.AND) {
                    patternsMap.put( var, modifiedPattern );
                }
            }
        }

        Condition condition = new CompositePatterns( type, conditions );
        if ( type == Type.AND ) {
            if ( topLevel && inputs.size() > usedVars.size() ) {
                inputs.keySet().removeAll( usedVars );
                for ( Map.Entry<Variable<?>, InputViewItem<?>> entry : inputs.entrySet() ) {
                    conditions.add( 0, new PatternImpl( entry.getKey(), Constraint.EMPTY, entry.getValue().getDataSourceDefinition() ) );
                }
            }
        }
        return condition;
    }

    private static DataSourceDefinition getDataSourceDefinition( Map<Variable<?>, InputViewItem<?>> inputs, Variable var ) {
        InputViewItem input = inputs.get(var);
        return input != null ? input.getDataSourceDefinition() : DataSourceDefinitionImpl.DEFAULT;
    }

    private static Pattern expr2Pattern(ExprViewItem viewItem, Pattern pattern) {
        if (viewItem instanceof Expr1ViewItemImpl ) {
            Expr1ViewItemImpl expr = (Expr1ViewItemImpl)viewItem;
            ( (PatternImpl) pattern ).addConstraint( new SingleConstraint1( expr ) );
        } else if (viewItem instanceof Expr2ViewItemImpl ) {
            Expr2ViewItemImpl expr = (Expr2ViewItemImpl)viewItem;
            ( (PatternImpl) pattern ).addConstraint( new SingleConstraint2( expr ) );
        } else if (viewItem instanceof AccumulateExprViewItem) {
            AccumulateExprViewItem acc = (AccumulateExprViewItem)viewItem;
            pattern = new AccumulatePatternImpl(expr2Pattern(acc.getExpr(), pattern), acc.getFunctions());
        }

        return pattern;
    }
}
