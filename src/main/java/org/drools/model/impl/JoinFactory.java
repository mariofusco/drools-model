package org.drools.model.impl;

import org.drools.model.JoinPattern;
import org.drools.model.Type;
import org.drools.model.Variable;

import static org.drools.model.DSL.bind;

public class JoinFactory {

    private final Variable[] vars;

    public JoinFactory(Variable... vars) {
        this.vars = vars;
    }

    public <T> JoinPattern<T> filter(Variable<T> var) {
        return new JoinPatternImpl<T>(vars, var);
    }

    public <T> JoinPattern<T> filter(Type<T> type) {
        return filter(bind(type));
    }
}
