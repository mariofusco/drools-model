package org.drools.model.impl;

import org.drools.model.*;

import java.util.*;

public class CollectionObjectSource implements ObjectSource {
    private final Collection source;

    public CollectionObjectSource(Collection source) {
        this.source = source;
    }

    public static ObjectSource sourceOf(Object... items) {
        return new CollectionObjectSource(Arrays.asList(items));
    }

    public Collection getObjects() {
        return source;
    }
}
