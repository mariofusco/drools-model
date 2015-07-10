package org.drools.model.constraints;

import org.drools.model.Variable;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.PredicateN;

import static org.drools.model.functions.LambdaIntrospector.getLambdaFingerprint;

public class SingleConstraint1<A> extends AbstractSingleConstraint {

    private final Variable<A> variable;
    private final Predicate1<A> predicate;

    public SingleConstraint1(Variable<A> variable, Predicate1<A> predicate) {
        super(getLambdaFingerprint(predicate, variable));
        this.variable = variable;
        this.predicate = predicate;
    }

    public SingleConstraint1(String exprId, Variable<A> variable, Predicate1<A> predicate) {
        super(exprId);
        this.variable = variable;
        this.predicate = predicate;
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }

    @Override
    public Variable[] getVariables() {
        return new Variable[] { variable };
    }

    @Override
    public PredicateN getPredicate() {
        return new PredicateN() {
            @Override
            public boolean test(Object... objs) {
                return predicate.test((A)objs[0]);
            }
        };
    }
}
