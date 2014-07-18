package org.drools.model.flow;

import org.drools.model.Variable;

public interface ViewItem<T> {
    Variable<T> getFirstVariable();
}
