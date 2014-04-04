package org.drools.model;

import java.util.Collection;

// DataSource == EntryPoint
public interface DataSource {
    Collection<ObjectSource> getObjectSource();
}
