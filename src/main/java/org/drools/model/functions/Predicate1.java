package org.drools.model.functions;

import java.io.Serializable;

public interface Predicate1<A> extends Serializable {
    boolean test(A a);

    public Predicate1 TRUE = new Predicate1() {
        @Override
        public boolean test(Object t) {
            return true;
        }
    };
}
