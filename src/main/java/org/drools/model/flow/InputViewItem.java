package org.drools.model.flow;

import org.drools.model.DataSource;
import org.drools.model.Variable;
import org.drools.model.functions.Function0;

public class InputViewItem<T> implements ViewItem {
    private final Variable<T> var;
    private final Function0<DataSource<T>> provider;

    public InputViewItem(Variable<T> var, Function0<DataSource<T>> provider) {
        this.var = var;
        this.provider = provider;
    }

    @Override
    public Variable getFirstVariable() {
        return var;
    }

    public DataSource<T> getDataSource() {
        return provider.apply();
    }
}
