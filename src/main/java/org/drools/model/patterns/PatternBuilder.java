package org.drools.model.patterns;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Index;
import org.drools.model.Pattern;
import org.drools.model.SingleConstraint;
import org.drools.model.Type;
import org.drools.model.Variable;
import org.drools.model.constraints.AbstractConstraint;
import org.drools.model.constraints.AbstractSingleConstraint;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.functions.Function0;
import org.drools.model.functions.Function1;
import org.drools.model.functions.Function2;
import org.drools.model.functions.FunctionN;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;
import org.drools.model.index.AlphaIndexImpl;
import org.drools.model.index.BetaIndexImpl;

import static org.drools.model.DSL.bind;
import static org.drools.model.functions.FunctionUtils.toFunctionN;

public class PatternBuilder {

    private Variable[] joinVars;
    private Function0<DataSource> dataSourceSupplier;

    public PatternBuilder from(Function0<DataSource> dataSourceSupplier) {
        this.dataSourceSupplier = dataSourceSupplier;
        return this;
    }

    public <T> BoundPatternBuilder<T> filter(Type<T> type) {
        return filter((Variable<T>) bind(type));
    }

    public <T> BoundPatternBuilder<T> filter(Variable<T> var) {
        return new BoundPatternBuilder<T>(var, dataSourceSupplier);
    }

    public <T> InvokerPatternBuilder<T> set(Variable<T> var) {
        return new InvokerPatternBuilder<T>(var, dataSourceSupplier);
    }

    public interface ValidBuilder<T> {
        Pattern<T> get();
    }

    public static class InvokerPatternBuilder<T> {
        protected final Variable<T> variable;
        protected Function0<DataSource> dataSourceSupplier;
        protected Variable[] inputVariables;

        private InvokerPatternBuilder(Variable<T> variable, Function0<DataSource> dataSourceSupplier) {
            this.variable = variable;
            this.dataSourceSupplier = dataSourceSupplier;
        }

        public <A> InvokerSingleValuePatternBuilder<T> invoking(Function0<T> f) {
            return new InvokerSingleValuePatternBuilder(variable, dataSourceSupplier, new Variable[0], toFunctionN(f));
        }

        public <A> InvokerSingleValuePatternBuilder<T> invoking(Variable<A> var, Function1<A, T> f) {
            return new InvokerSingleValuePatternBuilder(variable, dataSourceSupplier, new Variable[] { var }, toFunctionN(f));
        }

        public <A, B> InvokerSingleValuePatternBuilder<T> invoking(Variable<A> var1, Variable<B> var2, Function2<A, B, T> f) {
            return new InvokerSingleValuePatternBuilder(variable, dataSourceSupplier, new Variable[] { var1, var2 }, toFunctionN(f));
        }

        public <A> InvokerMultiValuePatternBuilder<T> in(Function0<Iterable<? extends T>> f) {
            return new InvokerMultiValuePatternBuilder(variable, dataSourceSupplier, new Variable[0], toFunctionN(f));
        }

        public <A> InvokerMultiValuePatternBuilder<T> in(Variable<A> var, Function1<A, Iterable<? extends T>> f) {
            return new InvokerMultiValuePatternBuilder(variable, dataSourceSupplier, new Variable[] { var }, toFunctionN(f));
        }

        public <A, B> InvokerMultiValuePatternBuilder<T> in(Variable<A> var1, Variable<B> var2, Function2<A, B, Iterable<? extends T>> f) {
            return new InvokerMultiValuePatternBuilder(variable, dataSourceSupplier, new Variable[] { var1, var2 }, toFunctionN(f));
        }
    }

    public static class InvokerSingleValuePatternBuilder<T> extends InvokerPatternBuilder<T> implements ValidBuilder<T> {
        private FunctionN<T> function;

        public InvokerSingleValuePatternBuilder(Variable<T> variable, Function0<DataSource> dataSourceSupplier, Variable[] inputVariables, FunctionN<T> function) {
            super(variable, dataSourceSupplier);
            this.inputVariables = inputVariables;
            this.function = function;
        }

        @Override
        public Pattern<T> get() {
            return new InvokerSingleValuePatternImpl<T>(dataSourceSupplier, function, variable, inputVariables);
        }
    }

    public static class InvokerMultiValuePatternBuilder<T> extends InvokerPatternBuilder<T> implements ValidBuilder<T> {
        private FunctionN<Iterable<? extends T>> function;

        public InvokerMultiValuePatternBuilder(Variable<T> variable, Function0<DataSource> dataSourceSupplier, Variable[] inputVariables, FunctionN<Iterable<? extends T>> function) {
            super(variable, dataSourceSupplier);
            this.inputVariables = inputVariables;
            this.function = function;
        }

        @Override
        public Pattern<T> get() {
            return new InvokerMultiValuePatternImpl<T>(dataSourceSupplier, function, variable, inputVariables);
        }
    }

    public static class BoundPatternBuilder<T> implements ValidBuilder<T> {
        private final Variable<T> variable;
        private Function0<DataSource> dataSourceSupplier;

        private BoundPatternBuilder(Variable<T> variable, Function0<DataSource> dataSourceSupplier) {
            this.variable = variable;
            this.dataSourceSupplier = dataSourceSupplier;
        }

        public BoundPatternBuilder<T> from(Function0<DataSource> dataSourceSupplier) {
            this.dataSourceSupplier = dataSourceSupplier;
            return this;
        }

        public ConstrainedPatternBuilder<T> with(SingleConstraint constraint) {
            return new ConstrainedPatternBuilder(variable, (AbstractSingleConstraint)constraint, dataSourceSupplier);
        }

        public ConstrainedPatternBuilder<T> with(Predicate1<T> predicate) {
            return with(new SingleConstraint1<T>(variable, predicate));
        }

        public <A, B> ConstrainedPatternBuilder<T> with(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return with(new SingleConstraint2<A, B>(var1, var2, predicate));
        }

        public <A> ConstrainedPatternBuilder<T> with(Variable<A> var2, Predicate2<T, A> predicate) {
            return with(new SingleConstraint2<T, A>(variable, var2, predicate));
        }

        public ConstrainedPatternBuilder<T> with(String exprId, Predicate1<T> predicate) {
            return with(new SingleConstraint1<T>(exprId, variable, predicate));
        }

        public <A, B> ConstrainedPatternBuilder<T> with(String exprId, Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return with(new SingleConstraint2<A, B>(exprId, var1, var2, predicate));
        }

        public <A> ConstrainedPatternBuilder<T> with(String exprId, Variable<A> var2, Predicate2<T, A> predicate) {
            return with(new SingleConstraint2<T, A>(exprId, variable, var2, predicate));
        }

        @Override
        public Pattern<T> get() {
            return new PatternImpl(variable, AbstractSingleConstraint.EMPTY, dataSourceSupplier);
        }
    }

    public static class ConstrainedPatternBuilder<T> implements ValidBuilder<T> {
        private final Variable<T> variable;
        private Constraint constraint;
        private Function0<DataSource> dataSourceSupplier;
        private AbstractSingleConstraint lastConstraint;

        private ConstrainedPatternBuilder(Variable<T> variable, AbstractSingleConstraint constraint, Function0<DataSource> dataSourceSupplier) {
            this.variable = variable;
            this.constraint = constraint;
            this.lastConstraint = constraint;
            this.dataSourceSupplier = dataSourceSupplier;
        }

        public ConstrainedPatternBuilder<T> from(Function0<DataSource> dataSourceSupplier) {
            this.dataSourceSupplier = dataSourceSupplier;
            return this;
        }

        public ConstrainedPatternBuilder<T> and(Predicate1<T> predicate) {
            return and(new SingleConstraint1<T>(variable, predicate));
        }

        public ConstrainedPatternBuilder<T> and(String exprId, Predicate1<T> predicate) {
            return and(new SingleConstraint1<T>(exprId, variable, predicate));
        }

        public ConstrainedPatternBuilder<T> and(Constraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).and(constraint);
            return this;
        }

        public ConstrainedPatternBuilder<T> or(Predicate1<T> predicate) {
            return or(new SingleConstraint1<T>(variable, predicate));
        }

        public ConstrainedPatternBuilder<T> or(String exprId, Predicate1<T> predicate) {
            return or(new SingleConstraint1<T>(exprId, variable, predicate));
        }

        public ConstrainedPatternBuilder<T> or(Constraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).or(constraint);
            return this;
        }

        public <A, B> ConstrainedPatternBuilder<T> and(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return and(lastConstraint = new SingleConstraint2<A, B>(var1, var2, predicate));
        }

        public <A> ConstrainedPatternBuilder<T> and(Variable<A> var1, Predicate2<T, A> predicate) {
            return and(lastConstraint = new SingleConstraint2<T, A>(variable, var1, predicate));
        }

        public <A, B> ConstrainedPatternBuilder<T> and(String exprId, Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return and(lastConstraint = new SingleConstraint2<A, B>(exprId, var1, var2, predicate));
        }

        public <A> ConstrainedPatternBuilder<T> and(String exprId, Variable<A> var1, Predicate2<T, A> predicate) {
            return and(lastConstraint = new SingleConstraint2<T, A>(exprId, variable, var1, predicate));
        }

        public ConstrainedPatternBuilder<T> and(AbstractConstraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).and(constraint);
            return this;
        }

        public <A, B> ConstrainedPatternBuilder<T> or(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return or(lastConstraint = new SingleConstraint2<A, B>(var1, var2, predicate));
        }

        public <A> ConstrainedPatternBuilder<T> or(Variable<A> var1, Predicate2<T, A> predicate) {
            return or(lastConstraint = new SingleConstraint2<T, A>(variable, var1, predicate));
        }

        public <A, B> ConstrainedPatternBuilder<T> or(String exprId, Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
            return or(lastConstraint = new SingleConstraint2<A, B>(exprId, var1, var2, predicate));
        }

        public <A> ConstrainedPatternBuilder<T> or(String exprId, Variable<A> var1, Predicate2<T, A> predicate) {
            return or(lastConstraint = new SingleConstraint2<T, A>(exprId, variable, var1, predicate));
        }

        public ConstrainedPatternBuilder<T> or(AbstractConstraint constraint) {
            this.constraint = ((AbstractConstraint)this.constraint).or(constraint);
            return this;
        }

        public ConstrainedPatternBuilder<T> indexedBy(Index.ConstraintType constraintType, Function1<T, ?> leftOperandExtractor, Object rightValue) {
            lastConstraint.setIndex(new AlphaIndexImpl<T>(constraintType, leftOperandExtractor, rightValue));
            return this;
        }

        public <A> ConstrainedPatternBuilder<T> indexedBy(Index.ConstraintType constraintType, Function1<T, ?> leftOperandExtractor, Function1<A, ?> rightOperandExtractor) {
            lastConstraint.setIndex(new BetaIndexImpl<T>(constraintType, leftOperandExtractor, rightOperandExtractor));
            return this;
        }

        @Override
        public Pattern<T> get() {
            return new PatternImpl(variable, constraint, dataSourceSupplier);
        }
    }
}
