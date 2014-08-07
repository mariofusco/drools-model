package org.drools.model.flow;

import org.drools.model.Variable;
import org.drools.model.functions.FunctionN;

public class SetViewItem<T> implements ViewItem<T> {

    private final FunctionN<T> invokedFunction;
    private final Variable<T> variable;
    private final Variable[] inputVariables;

    SetViewItem(FunctionN<T> function, Variable<T> boundVariable, Variable... inputVariables) {
        this.invokedFunction = function;
        this.variable = boundVariable;
        this.inputVariables = inputVariables;
    }

    @Override
    public Variable<T> getFirstVariable() {
        return variable;
    }

    public FunctionN<T> getInvokedFunction() {
        return invokedFunction;
    }

    public Variable[] getInputVariables() {
        return inputVariables;
    }
}
