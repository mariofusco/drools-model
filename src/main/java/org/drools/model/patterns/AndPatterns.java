package org.drools.model.patterns;

import java.util.Arrays;
import java.util.List;

import org.drools.model.Condition;
import org.drools.model.View;

public class AndPatterns implements Condition, View {

    private final List<Condition> patterns;

    public AndPatterns(Condition... patterns) {
        this( Arrays.asList(patterns) );
    }

    public AndPatterns(List<Condition> patterns) {
        this.patterns = patterns;
    }

    @Override
    public List<Condition> getSubConditions() {
        return patterns;
    }

    @Override
    public Type getType() {
        return Type.AND;
    }
}
