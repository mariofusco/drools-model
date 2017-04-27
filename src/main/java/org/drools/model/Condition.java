package org.drools.model;

import java.util.List;

public interface Condition {
    List<Condition> getSubConditions();

    Type getType();

    enum Type {
        PATTERN, OOPATH, OR, AND
    }
}
