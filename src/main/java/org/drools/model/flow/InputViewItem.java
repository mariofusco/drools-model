package org.drools.model.flow;

import org.drools.model.DataSource;
import org.drools.model.Variable;
import org.drools.model.functions.Provider;

public class InputViewItem<T> implements ViewItem {
    private final Variable<T> var;
    private final Provider<DataSource<T>> provider;

    public InputViewItem(Variable<T> var, Provider<DataSource<T>> provider) {
        this.var = var;
        this.provider = provider;
    }

    @Override
    public Variable getFirstVariable() {
        return var;
    }

    public DataSource<T> getDataSource() {
        return provider.provide();
    }
}
