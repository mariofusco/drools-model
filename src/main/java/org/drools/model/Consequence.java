package org.drools.model;

public interface Consequence {
    Variable[] getDeclarations();
    Block getBlock();
}
