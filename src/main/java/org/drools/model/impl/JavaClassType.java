package org.drools.model.impl;

import org.drools.model.Type;

public class JavaClassType<T> implements Type<T> {

    private final Class<T> type;

    private JavaClassType(Class<T> type) {
        this.type = type;
    }

    public static final <T> JavaClassType<T> typeOf(Class<T> type) {
        return new JavaClassType<T>(type);
    }
}
