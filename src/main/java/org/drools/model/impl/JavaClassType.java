package org.drools.model.impl;

import org.drools.model.Type;

public class JavaClassType implements Type {

    private final Class<?> type;

    private JavaClassType(Class<?> type) {
        this.type = type;
    }

    public static final JavaClassType typeOf(Class<?> type) {
        return new JavaClassType(type);
    }
}
