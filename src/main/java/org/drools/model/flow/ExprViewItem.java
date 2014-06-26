package org.drools.model.flow;

import org.drools.model.Condition;

public interface ExprViewItem extends ViewItem {
    Condition.Type getType();
}
