package org.drools.model;

import java.util.List;

public interface Constraint {
    enum Type { TRUE, SINGLE, OR, AND }

    List<Constraint> getChildren();

    Type getType();

    Constraint True = new Constraint() {
        @Override
        public List<Constraint> getChildren() {
            return null;
        }

        @Override
        public Type getType() {
            return Type.TRUE;
        }
    };
}
