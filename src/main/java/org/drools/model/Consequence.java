package org.drools.model;

import org.drools.model.functions.Block;

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
