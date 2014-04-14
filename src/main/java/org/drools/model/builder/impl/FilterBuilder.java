package org.drools.model.builder.impl;

import org.drools.model.*;
import org.drools.model.builder.*;

public class FilterBuilder <T, F extends Filterable> {

    private final F filterable;

    public FilterBuilder(F filterable) {
        this.filterable = filterable;
    }

    public F equalTo(Expression expression) {
        filterable.addFilter(expression);
        return filterable;
    }
}
