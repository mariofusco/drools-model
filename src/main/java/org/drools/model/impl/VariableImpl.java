package org.drools.model.impl;

import org.drools.model.Type;
import org.drools.model.Variable;

public abstract class VariableImpl<T> implements Variable<T> {

    private static int variableIndex = 0;

    private final Type<T> type;
    private final String name;

    public VariableImpl(Type<T> type) {
        this(type, generateName());
    }

    public VariableImpl(Type<T> type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Type<T> getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Variable " + name + " of type " + type;
    }

    private static String generateName() {
        return "$" + variableIndex++ + "$";
    }
}
