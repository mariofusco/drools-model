package org.drools.model.impl;

import org.drools.model.Block;
import org.drools.model.Consequence;
import org.drools.model.Variable;

public class ConsequenceImpl implements Consequence {
    private final Variable[] declarations;
    private final Block block;

    public ConsequenceImpl(Block block, Variable... declarations) {
        this.declarations = declarations;
        this.block = block;
    }

    @Override
    public Variable[] getDeclarations() {
        return declarations;
    }

    @Override
    public Block getBlock() {
        return block;
    }
}
