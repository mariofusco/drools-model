package org.drools.model;

import java.util.Collection;

public interface ObjectSource {
    Collection<? extends Object> getObjects();
}
