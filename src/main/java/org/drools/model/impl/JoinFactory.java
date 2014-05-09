package org.drools.model.impl;

import org.drools.model.Type;
import org.drools.model.Variable;
import org.drools.model.patterns.JoinPatternBuilder;
import org.drools.model.patterns.JoinPatternImpl;

import static org.drools.model.DSL.bind;

public class JoinFactory {

    private final Variable[] vars;

    public JoinFactory(Variable... vars) {
        this.vars = vars;
    }

    public <T> JoinPatternBuilder<T> filter(Variable<T> var) {
        return new JoinPatternImpl<T>(vars, var);
    }

    public <T> JoinPatternBuilder<T> filter(Type<T> type) {
        return filter(bind(type));
    }
}
