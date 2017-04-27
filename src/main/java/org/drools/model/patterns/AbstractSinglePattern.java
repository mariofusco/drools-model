package org.drools.model.patterns;

import java.util.Collections;
import java.util.List;

import org.drools.model.Condition;
import org.drools.model.Condition.Type;

public abstract class AbstractSinglePattern {

    public List<Condition> getSubConditions() {
        return Collections.emptyList();
    }

    public Condition.Type getType() {
        return Type.PATTERN;
    }
}
