package org.drools.model.consequences;

import org.drools.model.functions.Block;
import org.drools.model.Consequence;
import org.drools.model.Type;
import org.drools.model.Variable;
import org.drools.model.functions.Block0;
import org.drools.model.functions.Block1;
import org.drools.model.functions.Block2;

import java.util.ArrayList;
import java.util.List;

public class ConsequenceBuilder {

    public _0 execute(Block0 block) {
        return new _0(block);
    }

    public <A> _1<A> on(Variable<A> dec1) {
        return new _1(dec1);
    }

    public <A, B> _2<A, B> on(Variable<A> decl1, Variable<B> decl2) {
        return new _2(decl1, decl2);
    }

    public interface ValidBuilder {
        Consequence get();
    }

    public class _0 implements ValidBuilder {
        private final Block block;

        public _0(Block0 block) {
            this.block = new Block() {
                @Override
                public void execute(Object... objs) {
                    block.execute();
                }
            };
        }

        @Override
        public Consequence get() {
            return new ConsequenceImpl(block);
        }
    }

    public static abstract class AbstractValidBuilder implements ValidBuilder {
        private final Variable[] declarations;
        protected Block block;
        private Type[] inserts;
        private List<Consequence.Update> updates = new ArrayList<Consequence.Update>();
        private Variable[] deletes;

        private AbstractValidBuilder(Variable... declarations) {
            this.declarations = declarations;
        }

        @Override
        public Consequence get() {
            return new ConsequenceImpl(block, declarations, inserts, updates.toArray(new Consequence.Update[updates.size()]), deletes);
        }

        public AbstractValidBuilder inserts(Type... inserts) {
            this.inserts = inserts;
            return this;
        }

        public AbstractValidBuilder updates(Variable updatedVariable, String... updatedFields) {
            updates.add(new ConsequenceImpl.UpdateImpl(updatedVariable, updatedFields));
            return this;
        }

        public AbstractValidBuilder deletes(Variable... deletes) {
            this.deletes = deletes;
            return this;
        }
    }

    public static class _1<A> extends AbstractValidBuilder {
        private _1(Variable<A> declaration) {
            super(declaration);
        }

        public _1<A> execute(Block1<A> block) {
            this.block = new Block() {
                @Override
                public void execute(Object... objs) {
                    block.execute((A)objs[0]);
                }
            };
            return this;
        }
    }

    public static class _2<A, B> extends AbstractValidBuilder {
        private _2(Variable<A> decl1, Variable<B> decl2) {
            super(decl1, decl2);
        }

        public _2<A, B> execute(Block2<A, B> block) {
            this.block = new Block() {
                @Override
                public void execute(Object... objs) {
                    block.execute((A)objs[0], (B)objs[1]);
                }
            };
            return this;
        }
    }
}
