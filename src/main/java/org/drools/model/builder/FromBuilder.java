package org.drools.model.builder;

import org.drools.model.DataSource;
import org.drools.model.builder.impl.IndexBuilder;

public interface FromBuilder {
    WithSource source(DataSource dataSource);

    public static interface WithSource {
        IndexBuilder<? extends WithSource> index();
    }
}
