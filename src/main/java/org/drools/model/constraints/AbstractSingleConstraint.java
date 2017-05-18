package org.drools.model.constraints;

import java.util.Collections;
import java.util.List;

import org.drools.model.Constraint;
import org.drools.model.Index;
import org.drools.model.SingleConstraint;

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

    @Override
    public String getExprId() {
        return exprId;
    }
}
