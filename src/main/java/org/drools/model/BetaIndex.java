package org.drools.model;

import org.drools.model.functions.Function1;

public interface BetaIndex<A> extends Index<A> {

    Function1 getRightOperandExtractor();
}