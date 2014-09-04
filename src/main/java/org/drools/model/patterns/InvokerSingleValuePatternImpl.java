package org.drools.model.patterns;

import org.drools.datasource.DataSource;
import org.drools.model.DataSourceDefinition;
import org.drools.model.InvokerSingleValuePattern;
import org.drools.model.Variable;
import org.drools.model.functions.Function0;
import org.drools.model.functions.FunctionN;

public class InvokerSingleValuePatternImpl<T> extends InvokerPatternImpl<T> implements InvokerSingleValuePattern<T> {
    private final FunctionN<T> invokedFunction;

    InvokerSingleValuePatternImpl(DataSourceDefinition dataSourceDefinition, FunctionN<T> function, Variable<T> boundVariable, Variable... inputVariables) {
        super(dataSourceDefinition, boundVariable, inputVariables);
        this.invokedFunction = function;
    }

    @Override
    public FunctionN<T> getInvokedFunction() {
        return invokedFunction;
    }

    @Override
    public boolean isMultiValue() {
        return false;
    }
}
