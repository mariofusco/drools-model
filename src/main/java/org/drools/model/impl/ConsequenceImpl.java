package org.drools.model.impl;

import org.drools.model.Block;
import org.drools.model.Consequence;
import org.drools.model.Variable;

public class ConsequenceImpl implements Consequence {
    private final Variable[] variables;
    private final Block block;

    public ConsequenceImpl(Block block, Variable... variables) {
        this.variables = variables;
        this.block = block;
    }

    @Override
    public Variable[] getVariables() {
        return variables;
    }

    @Override
    public Block getBlock() {
        return block;
    }
}
