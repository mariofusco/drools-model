package org.drools.model;

public interface Consequence {

    Variable[] getDeclarations();

    Block getBlock();

    Type[] getInserts();

    Update[] getUpdates();

    Variable[] getDeletes();

    boolean isChangingWorkingMemory();

    public interface Update<T> {
        Variable<T> getUpdatedVariable();

        String[] getUpdatedFields();
    }
}
