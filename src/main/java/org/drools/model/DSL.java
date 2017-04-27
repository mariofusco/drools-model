package org.drools.model;

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
import org.drools.model.impl.DataSourceDefinitionImpl;
import org.drools.model.impl.JavaClassType;
import org.drools.model.impl.RuleBuilder;
import org.drools.model.impl.SourceImpl;
import org.drools.model.impl.VariableImpl;
import org.drools.model.view.AccumulateExprViewItem;
import org.drools.model.view.CombinedExprViewItem;
import org.drools.model.view.Expr1ViewItem;
import org.drools.model.view.Expr2ViewItem;
import org.drools.model.view.ExprViewItem;
import org.drools.model.view.InputViewItem;
import org.drools.model.view.OOPathBuilder;
import org.drools.model.view.SetViewItem;
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
        return variableOf( type( type ) );
    }

    public static <T> Variable<T> variableOf( Type<T> type ) {
        return new VariableImpl<T>(type);
    }

    public static <T> Source<T> sourceOf( Type<T> type ) {
        return new SourceImpl<T>(type);
    }

    public static <T> Type<T> type( Class<T> type ) {
        return new JavaClassType<T>(type);
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

    public static <T> ExprViewItem<T> expr(Variable<T> var, Predicate1<T> predicate) {
        return new Expr1ViewItem<T>(var, predicate);
    }

    public static <T, U> ExprViewItem<T> expr(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return new Expr2ViewItem<T, U>(var1, var2, predicate);
    }

    public static <T> ExprViewItem<T> expr(String exprId, Variable<T> var, Predicate1<T> predicate) {
        return new Expr1ViewItem<T>(exprId, var, predicate);
    }

    public static <T, U> ExprViewItem<T> expr(String exprId, Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return new Expr2ViewItem<T, U>(exprId, var1, var2, predicate);
    }

    public static <T> OOPathBuilder<T> var( Variable<T> var ) {
        return new OOPathBuilder<T>(var);
    }

    public static <T> ExprViewItem<T> not(ExprViewItem expr) {
        return expr.setExistentialType(ExistentialPattern.ExistentialType.NOT);
    }

    public static <T> ExprViewItem<T> not(Variable<T> var) {
        return not(new Expr1ViewItem<T>("true", var, Predicate1.TRUE));
    }

    public static <T> ExprViewItem<T> not(Variable<T> var, Predicate1<T> predicate) {
        return not(new Expr1ViewItem<T>(var, predicate));
    }

    public static <T, U> ExprViewItem<T> not(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return not(new Expr2ViewItem<T, U>(var1, var2, predicate));
    }

    public static <T> ExprViewItem<T> exists(Variable<T> var) {
        return exists(new Expr1ViewItem<T>("true", var, Predicate1.TRUE));
    }

    public static <T> ExprViewItem<T> exists(ExprViewItem<T> expr) {
        return expr.setExistentialType(ExistentialPattern.ExistentialType.EXISTS);
    }

    public static <T> ExprViewItem<T> exists(Variable<T> var, Predicate1<T> predicate) {
        return exists(new Expr1ViewItem<T>(var, predicate));
    }

    public static <T, U> ExprViewItem<T> exists(Variable<T> var1, Variable<U> var2, Predicate2<T, U> predicate) {
        return exists(new Expr2ViewItem<T, U>(var1, var2, predicate));
    }

    public static <T> ExprViewItem<T> accumulate(ExprViewItem<T> expr, AccumulateFunction<T, ?, ?>... functions) {
        return new AccumulateExprViewItem(expr, functions);
    }

    public static ExprViewItem or(ExprViewItem... expressions) {
        return new CombinedExprViewItem(Condition.Type.OR, expressions);
    }

    public static ExprViewItem and(ExprViewItem... expressions) {
        return new CombinedExprViewItem(Condition.Type.AND, expressions);
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

    public static RuleBuilder rule(String name) {
        return new RuleBuilder(name);
    }

    public static RuleBuilder rule(String pkg, String name) {
        return new RuleBuilder(pkg, name);
    }
}
