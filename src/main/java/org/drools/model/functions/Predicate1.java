package org.drools.model.functions;

public interface Predicate1<A> {
    boolean test(A a);

    public Predicate1 TRUE = new Predicate1() {
        @Override
        public boolean test(Object t) {
            return true;
        }
    };
}
