package org.drools.model.patterns;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.drools.model.Condition;
import org.drools.model.Variable;
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
    public Variable<?>[] getBoundVariables() {
        return patterns.stream()
                     .flatMap( c -> Stream.of(c.getBoundVariables()) )
                     .distinct()
                     .toArray(Variable[]::new );
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
