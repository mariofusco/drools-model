package org.drools.model.patterns;

import java.util.Arrays;
import java.util.List;

import org.drools.model.Condition;
import org.drools.model.View;

public class CompositePatterns implements Condition, View {

    private final Type type;
    private final List<Condition> patterns;

    public CompositePatterns( Type type, Condition... patterns ) {
        this( type, Arrays.asList(patterns) );
    }

    public CompositePatterns( Type type, List<Condition> patterns ) {
        this.type = type;
        this.patterns = patterns;
    }

    @Override
    public List<Condition> getSubConditions() {
        return patterns;
    }

    @Override
    public Type getType() {
        return type;
    }
}
