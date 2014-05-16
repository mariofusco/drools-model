package org.drools.model;

import java.util.Collection;

// DataSource is an EntryPoint only supporting inserts (Stream of events)
public interface DataSource {
    Collection<ObjectSource> getObjectSources();
}
