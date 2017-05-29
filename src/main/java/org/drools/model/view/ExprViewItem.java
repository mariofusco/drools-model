package org.drools.model.view;

import org.drools.model.Condition;
import org.drools.model.ExistentialPattern;

public interface ExprViewItem<T> extends ViewItem<T> {
    Condition.Type getType();

    ExistentialPattern.ExistentialType getExistentialType();

    ExprViewItem setExistentialType(ExistentialPattern.ExistentialType existentialType);

    String getExprId();

    ExprViewItem reactOn(String... props);
}
