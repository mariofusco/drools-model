package org.drools.model.view;

import org.drools.model.DataSourceDefinition;
import org.drools.model.Variable;

public class InputViewItem<T> implements ViewItem {
    private final Variable<T> var;
    private final DataSourceDefinition dataSourceDefinition;

    public InputViewItem(Variable<T> var, DataSourceDefinition dataSourceDefinition) {
        this.var = var;
        this.dataSourceDefinition = dataSourceDefinition;
    }

    @Override
    public Variable getFirstVariable() {
        return var;
    }

    public DataSourceDefinition getDataSourceDefinition() {
        return dataSourceDefinition;
    }
}
