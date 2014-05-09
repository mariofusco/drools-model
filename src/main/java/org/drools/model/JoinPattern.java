package org.drools.model;

public interface JoinPattern<T> extends SinglePattern<T> {
    Variable[] getJoinVariables();
}
