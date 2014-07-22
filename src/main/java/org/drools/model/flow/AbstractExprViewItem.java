package org.drools.model.flow;

import org.drools.model.ExistentialPattern;
import org.drools.model.Variable;

import static java.util.UUID.randomUUID;

public abstract class AbstractExprViewItem<T> implements ExprViewItem<T>  {
    private final String exprId;

    private final Variable<T> var;

    private ExistentialPattern.ExistentialType existentialType;

    public AbstractExprViewItem(Variable<T> var) {
        this(randomUUID().toString(), var);
    }

    public AbstractExprViewItem(String exprId, Variable<T> var) {
        this.exprId = exprId;
        this.var = var;
    }

    @Override
    public Variable getFirstVariable() {
        return var;
    }

    @Override
    public ExistentialPattern.ExistentialType getExistentialType() {
        return existentialType;
    }

    @Override
    public AbstractExprViewItem<T> setExistentialType(ExistentialPattern.ExistentialType existentialType) {
        this.existentialType = existentialType;
        return this;
    }

    @Override
    public String getExprId() {
        return exprId;
    }
}
