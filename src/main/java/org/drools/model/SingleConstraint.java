package org.drools.model;

import org.drools.model.functions.MultiValuePredicate;

public interface SingleConstraint extends Constraint {
    Variable[] getVariables();

    MultiValuePredicate getPredicate();

    Index getIndex();
}
