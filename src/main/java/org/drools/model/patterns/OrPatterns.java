package org.drools.model.patterns;

import org.drools.model.Condition;

import java.util.Arrays;
import java.util.List;

public class OrPatterns implements Condition {

    private final List<Condition> patterns;

    public OrPatterns(Condition... patterns) {
        this.patterns = Arrays.asList(patterns);
    }

    @Override
    public List<Condition> getSubConditions() {
        return patterns;
    }

    @Override
    public Type getType() {
        return OrType.INSTANCE;
    }
}