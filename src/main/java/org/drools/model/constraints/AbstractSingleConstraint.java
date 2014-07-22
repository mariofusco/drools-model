package org.drools.model.constraints;

import org.drools.model.Constraint;
import org.drools.model.Index;
import org.drools.model.SingleConstraint;
import org.drools.model.Variable;
import org.drools.model.functions.PredicateN;

import java.util.Collections;
import java.util.List;

import static java.util.UUID.randomUUID;

public abstract class AbstractSingleConstraint extends AbstractConstraint implements SingleConstraint {

    private final String exprId;

    private Index index;

    protected AbstractSingleConstraint() {
        this(randomUUID().toString());
    }

    protected AbstractSingleConstraint(String exprId) {
        this.exprId = exprId;
    }

    @Override
    public List<Constraint> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }

    @Override
    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public static final SingleConstraint EMPTY = new AbstractSingleConstraint() {
        @Override
        public Variable[] getVariables() {
            return new Variable[0];
        }

        @Override
        public PredicateN getPredicate() {
            return PredicateN.True;
        }
    };

    @Override
    public String getExprId() {
        return exprId;
    }
}
