package org.drools.model;

import java.util.Collection;

public interface Constraint {
    enum Type { SINGLE, OR, AND }

    Collection<? extends Constraint> getChildren();

    Type getType();
}
