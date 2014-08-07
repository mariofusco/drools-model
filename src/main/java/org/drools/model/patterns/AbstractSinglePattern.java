package org.drools.model.patterns;

import org.drools.model.Condition;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSinglePattern {

    public List<Condition> getSubConditions() {
        return Collections.emptyList();
    }

    public Condition.Type getType() {
        return Condition.SingleType.INSTANCE;
    }
}
