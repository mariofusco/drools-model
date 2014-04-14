package org.drools.model.impl;

import org.drools.model.*;

import java.util.*;

public class FromModelImpl<T> implements FromModel<T> {

    private final Type<T> type;
    private final DataSource dataSource;
    private List<IndexModel> indexes;

    public FromModelImpl(Type<T> type, DataSource dataSource) {
        this.type = type;
        this.dataSource = dataSource;
    }

    public FromModelImpl(Type type, DataSource dataSource, List<IndexModel> indexes) {
        this(type, dataSource);
        this.indexes = indexes;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public List<Processor> getProcessors() {
        return null;
    }
}
