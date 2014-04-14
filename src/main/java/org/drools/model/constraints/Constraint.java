package org.drools.model.constraints;

public abstract class Constraint {

    public OrConstraints or(Constraint constraint) {
        return new OrConstraints(this, constraint);
    }

    public AndConstraints and(Constraint constraint) {
        return new AndConstraints(this, constraint);
    }

    public static AndConstraints and(Constraint... constraints) {
        return new AndConstraints(constraints);
    }

    public static OrConstraints or(Constraint... constraints) {
        return new OrConstraints(constraints);
    }
}