package org.drools.model.flow;

import org.drools.model.ExistentialPattern;
import org.drools.model.Variable;

public abstract class AbstractExprViewItem<T> implements ExprViewItem  {
    private final Variable<T> var;

    private ExistentialPattern.ExistentialType existentialType;

    public AbstractExprViewItem(Variable<T> var) {
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

    public AbstractExprViewItem<T> setExistentialType(ExistentialPattern.ExistentialType existentialType) {
        this.existentialType = existentialType;
        return this;
    }
}
