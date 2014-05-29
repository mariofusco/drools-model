package org.drools.model.patterns;

import org.drools.model.Condition;
import org.drools.model.View;

import java.util.Arrays;
import java.util.List;

public class AndPatterns implements Condition, View {

    private final List<Condition> patterns;

    public AndPatterns(Condition... patterns) {
        this.patterns = Arrays.asList(patterns);
    }

    @Override
    public List<Condition> getSubConditions() {
        return patterns;
    }

    @Override
    public Type getType() {
        return AndType.INSTANCE;
    }
}
