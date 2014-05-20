package org.drools.model.consequences;

import org.drools.model.functions.Block;
import org.drools.model.Consequence;
import org.drools.model.Type;
import org.drools.model.Variable;

public class ConsequenceImpl implements Consequence {
    private final Variable[] declarations;
    private final Block block;

    private final Type[] inserts;
    private final Update[] updates;
    private final Variable[] deletes;

    ConsequenceImpl(Block block) {
        this(block, null, null, null, null);
    }

    ConsequenceImpl(Block block, Variable[] declarations, Type[] inserts, Update[] updates, Variable[] deletes) {
        this.declarations = declarations;
        this.block = block;
        this.inserts = inserts == null ? new Type[0] : inserts;
        this.updates = updates == null ? new Update[0] : updates;
        this.deletes = deletes == null ? new Variable[0] : deletes;
    }

    @Override
    public Variable[] getDeclarations() {
        return declarations;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public Type[] getInserts() {
        return inserts;
    }

    @Override
    public Update[] getUpdates() {
        return updates;
    }

    @Override
    public Variable[] getDeletes() {
        return deletes;
    }

    @Override
    public boolean isChangingWorkingMemory() {
        return inserts.length > 0 || updates.length > 0 || deletes.length > 0;
    }

    public static class UpdateImpl implements Update {
        private final Variable updatedVariable;
        private final String[] updatedFields;

        public UpdateImpl(Variable updatedVariable, String... updatedFields) {
            this.updatedVariable = updatedVariable;
            this.updatedFields = updatedFields;
        }

        @Override
        public Variable getUpdatedVariable() {
            return updatedVariable;
        }

        @Override
        public String[] getUpdatedFields() {
            return updatedFields;
        }
    }
}
