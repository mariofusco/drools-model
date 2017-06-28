package org.drools.model;

import java.util.List;

public interface Condition {
    List<Condition> getSubConditions();

    Type getType();

    enum Type {
        PATTERN( false ), ACCUMULATE( false ), OOPATH( false ), OR( true ), AND( true ), NOT( true ), EXISTS( true );

        private final boolean composite;

        Type( boolean composite ) {
            this.composite = composite;
        }

        public boolean isComposite() {
            return composite;
        }
    }
}
