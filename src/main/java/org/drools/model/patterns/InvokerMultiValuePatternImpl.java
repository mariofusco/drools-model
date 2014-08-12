package org.drools.model.patterns;

import org.drools.model.DataSource;
import org.drools.model.InvokerMultiValuePattern;
import org.drools.model.Variable;
import org.drools.model.functions.Function0;
import org.drools.model.functions.FunctionN;

public class InvokerMultiValuePatternImpl<T> extends InvokerPatternImpl<T> implements InvokerMultiValuePattern<T> {
    private final FunctionN<Iterable<? extends T>> invokedFunction;

    InvokerMultiValuePatternImpl(Function0<DataSource> dataSourceSupplier, FunctionN<Iterable<? extends T>> function, Variable<T> boundVariable, Variable... inputVariables) {
        super(dataSourceSupplier, boundVariable, inputVariables);
        this.invokedFunction = function;
    }

    @Override
    public FunctionN<Iterable<? extends T>> getInvokedFunction() {
        return invokedFunction;
    }

    @Override
    public boolean isMultiValue() {
        return true;
    }
}
