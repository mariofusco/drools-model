package org.drools.model;

import org.drools.model.functions.FunctionN;

public interface InvokerPattern<T> extends Pattern<T> {

    FunctionN<T> getInvokedFunction();
}
