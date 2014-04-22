package org.drools.model;

public interface Constraint {
    enum Type { SIMPLE, OR, AND }

    Iterable<? extends Constraint> getChildren();

    Type getType();
}
