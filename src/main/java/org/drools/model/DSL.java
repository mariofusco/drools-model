package org.drools.model;

import java.util.concurrent.TimeUnit;

import org.drools.model.consequences.ConsequenceBuilder;
import org.drools.model.datasources.DataStore;
import org.drools.model.datasources.DataStream;
import org.drools.model.datasources.impl.DataStreamImpl;
import org.drools.model.datasources.impl.SetDataStore;
import org.drools.model.functions.Block0;
import org.drools.model.functions.Block1;
import org.drools.model.functions.Function0;
import org.drools.model.functions.Function1;
import org.drools.model.functions.Function2;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;
import org.drools.model.functions.temporal.AfterPredicate;
import org.drools.model.functions.temporal.BeforePredicate;
import org.drools.model.functions.temporal.Interval;
import org.drools.model.functions.temporal.TemporalPredicate;
import org.drools.model.impl.DataSourceDefinitionImpl;
import org.drools.model.impl.DeclarationImpl;
import org.drools.model.impl.GlobalImpl;
import org.drools.model.impl.JavaClassType;
import org.drools.model.impl.RuleBuilder;
import org.drools.model.impl.SourceImpl;
import org.drools.model.impl.WindowImpl;
import org.drools.model.view.AccumulateExprViewItem;
import org.drools.model.view.CombinedExprViewItem;
import org.drools.model.view.ExistentialExprViewItem;
import org.drools.model.view.Expr1ViewItem;
import org.drools.model.view.Expr1ViewItemImpl;
import org.drools.model.view.Expr2ViewItem;
import org.drools.model.view.Expr2ViewItemImpl;
import org.drools.model.view.ExprViewItem;
import org.drools.model.view.InputViewItem;
import org.drools.model.view.OOPathBuilder;
import org.drools.model.view.OOPathBuilder.OOPathChunkBuilder;
import org.drools.model.view.SetViewItem;
import org.drools.model.view.TemporalExprViewItem;
import org.drools.model.view.ViewItem;
import org.drools.model.view.ViewItemBuilder;

import static org.drools.model.functions.FunctionUtils.toFunctionN;
import static org.drools.model.impl.ViewBuilder.viewItems2Patterns;

public class DSL {

    // -- DataSource --

    public static <T> DataStore<T> storeOf( T... items ) {
        return SetDataStore.storeOf( items );
    }

    public static DataStore newDataStore() {
        return storeOf();
    }

    public static DataStream newDataStream() {
        return new DataStreamImpl();
    }

    // -- Variable --

    public static <T> Variable<T> any(Class<T> type) {
        return declarationOf( type( type ) );
    }

    public static <T> Declaration<T> declarationOf( Type<T> type ) {
        return new DeclarationImpl<T>( type );
    }

    public static <T> Declaration<T> declarationOf( Type<T> type, String entryPoint ) {
        return new DeclarationImpl<T>( type ).setEntryPoint( entryPoint );
    }

    public static <T> Declaration<T> declarationOf( Type<T> type, Window window ) {
        return new DeclarationImpl<T>( type ).setWindow( window );
    }

    public static <T> Declaration<T> declarationOf( Type<T> type, String entryPoint, Window window ) {
        return new DeclarationImpl<T>( type ).setEntryPoint( entryPoint ).setWindow( window );
    }

    public static <T> Global<T> globalOf( Type<T> type, String pkg ) {
        return new GlobalImpl<T>( type, pkg );
    }

    public static <T> Global<T> globalOf( Type<T> type, String pkg, String name ) {
        return new GlobalImpl<T>( type, pkg, name );
    }

    public static <T> Source<T> sourceOf( String name, Type<T> type ) {
        return new SourceImpl<T>( name, type);
    }

    public static <T> Type<T> type( Class<T> type ) {
        return new JavaClassType<T>(type);
    }

    public static Window window( Window.Type type, long value ) {
        return new WindowImpl(type, value);
    }

    public static Window window( Window.Type type, long value, TimeUnit timeUnit ) {
        return new WindowImpl(type, value, timeUnit);
    }

    // -- LHS --

    public static View when(ViewItem... viewItems) {
        return view(viewItems);
    }

    public static View view(ViewItemBuilder... viewItemBuilders ) {
        return viewItems2Patterns( viewItemBuilders );
    }

    public static <T> ViewItem<T> input(Variable<T> var) {
        return new InputViewItem<T>(var, DataSourceDefinitionImpl.DEFAULT);
    }

    public static <T> ViewItem<T> input(Variable<T> var, String dataSourceName) {
        return new InputViewItem<T>(var, new DataSourceDefinitionImpl(dataSourceName, false));
    }

    public static <T> ViewItem<T> subscribe(Variable<T> var, String dataSourceName) {
        return new InputViewItem<T>(var, new DataSourceDefinitionImpl(dataSourceName, true));
    }

    public static <T> Expr1ViewItem<T> expr( Variable<T> var, Predicate1<T> predicate ) {
        return new Expr1ViewItemImpl<T>( var, predicate );
    }

    public static <T, U> Expr2ViewItem<T, U> expr(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return new Expr2ViewItemImpl<T, U>( var1, var2, predicate );
    }

    public static <T> Expr1ViewItem<T> expr(String exprId, Variable<T> var, Predicate1<T> predicate) {
        return new Expr1ViewItemImpl<T>( exprId, var, predicate);
    }

    public static <T, U> Expr2ViewItem<T, U> expr( String exprId, Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate ) {
        return new Expr2ViewItemImpl<T, U>( exprId, var1, var2, predicate);
    }

    public static <T> OOPathChunkBuilder<T, T> from( Source<T> source ) {
        return new OOPathBuilder<T>(source).firstChunk();
    }

    public static ExprViewItem not(ExprViewItem expression, ExprViewItem... expressions) {
        return new ExistentialExprViewItem( Condition.Type.NOT, and( expression, expressions) );
    }

    public static <T> ExprViewItem<T> not(Variable<T> var) {
        return not(new Expr1ViewItemImpl<T>( "true", var, Predicate1.TRUE) );
    }

    public static <T> ExprViewItem<T> not(Variable<T> var, Predicate1<T> predicate) {
        return not(new Expr1ViewItemImpl<T>( var, predicate) );
    }

    public static <T, U> ExprViewItem<T> not(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return not(new Expr2ViewItemImpl<T, U>( var1, var2, predicate) );
    }

    public static ExprViewItem exists(ExprViewItem expression, ExprViewItem... expressions) {
        return new ExistentialExprViewItem( Condition.Type.EXISTS, and( expression, expressions) );
    }

    public static <T> ExprViewItem<T> exists(Variable<T> var) {
        return exists(new Expr1ViewItemImpl<T>( "true", var, Predicate1.TRUE) );
    }

    public static <T> ExprViewItem<T> exists(Variable<T> var, Predicate1<T> predicate) {
        return exists(new Expr1ViewItemImpl<T>( var, predicate) );
    }

    public static <T, U> ExprViewItem<T> exists(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return exists(new Expr2ViewItemImpl<T, U>( var1, var2, predicate) );
    }

    public static <T> ExprViewItem<T> accumulate(ExprViewItem<T> expr, AccumulateFunction<T, ?, ?>... functions) {
        return new AccumulateExprViewItem(expr, functions);
    }

    public static ExprViewItem or(ExprViewItem expression, ExprViewItem... expressions) {
        if (expressions == null || expressions.length == 0) {
            return expression;
        }
        return new CombinedExprViewItem(Condition.Type.OR, combineExprs( expression, expressions ) );
    }

    public static ExprViewItem and(ExprViewItem expression, ExprViewItem... expressions) {
        if (expressions == null || expressions.length == 0) {
            return expression;
        }
        return new CombinedExprViewItem(Condition.Type.AND, combineExprs( expression, expressions ) );
    }

    private static ExprViewItem[] combineExprs( ExprViewItem expression, ExprViewItem[] expressions ) {
        ExprViewItem[] andExprs = new ExprViewItem[expressions.length+1];
        andExprs[0] = expression;
        System.arraycopy( expressions, 0, andExprs, 1, expressions.length );
        return andExprs;
    }

    public static <T> SetViewItemBuilder<T> set(Variable<T> var) {
        return new SetViewItemBuilder<T>(var);
    }

    public static class SetViewItemBuilder<T> {
        private final Variable<T> var;

        private SetViewItemBuilder(Variable<T> var) {
            this.var = var;
        }

        public SetViewItem<T> invoking(Function0<T> f) {
            return new SetViewItem<T>(toFunctionN(f), false, var);
        }

        public <A> SetViewItem<T> invoking(Variable<A> var1, Function1<A, T> f) {
            return new SetViewItem<T>(toFunctionN(f), false, var, var1);
        }

        public <A, B> SetViewItem<T> invoking(Variable<A> var1, Variable<B> var2, Function2<A, B, T> f) {
            return new SetViewItem<T>(toFunctionN(f), false, var, var1, var2);
        }

        public SetViewItem<T> in(Function0<Iterable<? extends T>> f) {
            return new SetViewItem<T>(toFunctionN(f), true, var);
        }

        public <A> SetViewItem<T> in(Variable<A> var1, Function1<A, Iterable<? extends T>> f) {
            return new SetViewItem<T>(toFunctionN(f), true, var, var1);
        }

        public <A, B> SetViewItem<T> in(Variable<A> var1, Variable<B> var2, Function2<A, B, Iterable<? extends T>> f) {
            return new SetViewItem<T>(toFunctionN(f), true, var, var1, var2);
        }
    }

    // -- Temporal Constraints --

    public static <T> TemporalExprViewItem<T> expr( String exprId, Variable<T> var1, Variable<?> var2, TemporalPredicate temporalPredicate ) {
        return new TemporalExprViewItem<T>( exprId, var1, var2, temporalPredicate);
    }

    public static TemporalPredicate after() {
        return new AfterPredicate();
    }

    public static TemporalPredicate after( long lowerBound, long upperBound ) {
        return new AfterPredicate( new Interval( lowerBound, upperBound ) );
    }

    public static TemporalPredicate after( long lowerBound, TimeUnit lowerUnit, long upperBound, TimeUnit upperUnit ) {
        return new AfterPredicate( new Interval( lowerBound, lowerUnit, upperBound, upperUnit ) );
    }

    public static TemporalPredicate before() {
        return new BeforePredicate();
    }

    public static TemporalPredicate before(long lowerBound, long upperBound) {
        return new BeforePredicate( new Interval( lowerBound, upperBound ) );
    }

    public static TemporalPredicate before( long lowerBound, TimeUnit lowerUnit, long upperBound, TimeUnit upperUnit ) {
        return new BeforePredicate( new Interval( lowerBound, lowerUnit, upperBound, upperUnit ) );
    }

    // -- RHS --

    public static ConsequenceBuilder._0 execute(Block0 block) {
        return new ConsequenceBuilder._0(block);
    }

    public static ConsequenceBuilder._0 execute(Block1<Drools> block) {
        return new ConsequenceBuilder._0(block);
    }

    public static <A> ConsequenceBuilder._1<A> on(Variable<A> dec1) {
        return new ConsequenceBuilder._1(dec1);
    }

    public static <A, B> ConsequenceBuilder._2<A, B> on(Variable<A> decl1, Variable<B> decl2) {
        return new ConsequenceBuilder._2(decl1, decl2);
    }

    public static <A, B, C> ConsequenceBuilder._3<A, B, C> on(Variable<A> decl1, Variable<B> decl2, Variable<C> decl3) {
        return new ConsequenceBuilder._3(decl1, decl2, decl3);
    }

    public static RuleBuilder rule(String name) {
        return new RuleBuilder(name);
    }

    public static RuleBuilder rule(String pkg, String name) {
        return new RuleBuilder(pkg, name);
    }
}
