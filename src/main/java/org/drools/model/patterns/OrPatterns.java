package org.drools.model.patterns;

import java.util.Arrays;
import java.util.List;

import org.drools.model.Condition;

public class OrPatterns implements Condition {

    private final List<Condition> patterns;

    public OrPatterns(Condition... patterns) {
        this( Arrays.asList(patterns) );
    }

    public OrPatterns(List<Condition> patterns) {
        this.patterns = patterns;
    }

    @Override
    public List<Condition> getSubConditions() {
        return patterns;
    }

    @Override
    public Type getType() {
        return Type.OR;
    }
}