package org.drools.model;

import java.util.Collections;
import java.util.List;

public interface Condition {

    default List<Condition> getSubConditions() {
        return Collections.emptyList();
    }

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
