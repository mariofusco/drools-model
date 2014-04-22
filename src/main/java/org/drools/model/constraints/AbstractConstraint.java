package org.drools.model.constraints;

import org.drools.model.Constraint;

public abstract class AbstractConstraint implements Constraint {

    public OrConstraints or(AbstractConstraint constraint) {
        return new OrConstraints(this, constraint);
    }

    public AndConstraints and(AbstractConstraint constraint) {
        return new AndConstraints(this, constraint);
    }

    public static AndConstraints and(AbstractConstraint... constraints) {
        return new AndConstraints(constraints);
    }

    public static OrConstraints or(AbstractConstraint... constraints) {
        return new OrConstraints(constraints);
    }
}