package org.drools.model;

import java.util.Collection;

public interface Constraint {
    enum Type { TRUE, SINGLE, OR, AND }

    Collection<? extends Constraint> getChildren();

    Type getType();

    Constraint True = new Constraint() {
        @Override
        public Collection<? extends Constraint> getChildren() {
            return null;
        }

        @Override
        public Type getType() {
            return Type.TRUE;
        }
    };
}
