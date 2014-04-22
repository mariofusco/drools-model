package org.drools.model;

public interface SingleConstraint extends Constraint {
    Variable[] getVariables();

    Object getPredicate();
}
