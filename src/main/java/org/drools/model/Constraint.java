package org.drools.model;

import java.util.Collections;
import java.util.List;

public interface Constraint {
    enum Type { TRUE, SINGLE, OR, AND }

    List<Constraint> getChildren();

    Type getType();

    Constraint True = new Constraint() {
        @Override
        public List<Constraint> getChildren() {
            return Collections.emptyList();
        }

        @Override
        public Type getType() {
            return Type.TRUE;
        }
    };
}
