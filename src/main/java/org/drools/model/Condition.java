package org.drools.model;

import java.util.List;

public interface Condition {
    List<Condition> getSubConditions();

    Type getType();

    public interface Type { }

    public static class SingleType implements Type {
        public static final SingleType INSTANCE = new SingleType();
    }
    public static class OrType implements Type {
        public static final OrType INSTANCE = new OrType();
    }
    public static class AndType implements Type {
        public static final AndType INSTANCE = new AndType();
    }
}
