package org.drools.model.view;

import org.drools.model.Variable;

public interface ViewItem<T> extends ViewItemBuilder<T> {

    Variable<T> getFirstVariable();

    @Override
    default ViewItem<T> get() { return this; }
}
