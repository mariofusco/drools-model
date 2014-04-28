package org.drools.model;

public interface JoinPattern<T> extends Pattern<T> {
    Variable[] getJoinVariables();
}
