package org.drools.model.impl;

import org.drools.model.Type;
import org.drools.model.Variable;

public class VariableImpl<T> implements Variable<T> {
    private final Type<T> type;

    public VariableImpl(Type<T> type) {
        this.type = type;
    }

    @Override
    public Type<T> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Variable of type " + type;
    }
}
