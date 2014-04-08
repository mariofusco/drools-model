package org.drools.model.impl;

import org.drools.model.*;

import java.util.*;

public class DataSourceImpl implements DataSource {

    private final Collection<ObjectSource> sources = new ArrayList<ObjectSource>();

    private DataSourceImpl() { }

    @Override
    public Collection<ObjectSource> getObjectSources() {
        return sources;
    }

    public DataSourceImpl addObjectSource(ObjectSource source) {
        sources.add(source);
        return this;
    }

    public static final DataSource dataSource(ObjectSource... sources) {
        DataSourceImpl dataSource = new DataSourceImpl();
        for (ObjectSource source : sources) {
            dataSource.addObjectSource(source);
        }
        return dataSource;
    }
}
