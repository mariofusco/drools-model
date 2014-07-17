package org.drools.model.functions;

public interface PredicateN {
    boolean test(Object... objs);

    PredicateN True = new PredicateN() {
        @Override
        public boolean test(Object... objs) {
            return true;
        }
    };
}
