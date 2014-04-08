package org.drools.model.builder;

import org.drools.model.*;
import org.drools.model.builder.impl.IndexBuilder;

public interface FromBuilder {
    WithSource source(DataSource dataSource);

    public static interface WithSource<T> {
        IndexBuilder<T, ? extends WithSource<T>> index();
        FromModel build();
    }
}
