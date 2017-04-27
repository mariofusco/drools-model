package org.drools.model.impl;

import org.drools.model.Source;
import org.drools.model.Type;

public class SourceImpl<T> implements Source<T> {
    private final Type<T> type;

    public SourceImpl( Type<T> type ) {
        this.type = type;
    }

    @Override
    public Type<T> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Source of type " + type;
    }
}
