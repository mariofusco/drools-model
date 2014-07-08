package org.drools.model.flow;

import org.drools.model.Condition;
import org.drools.model.ExistentialPattern;

public interface ExprViewItem extends ViewItem {
    Condition.Type getType();

    ExistentialPattern.ExistentialType getExistentialType();

    ExprViewItem setExistentialType(ExistentialPattern.ExistentialType existentialType);
}
