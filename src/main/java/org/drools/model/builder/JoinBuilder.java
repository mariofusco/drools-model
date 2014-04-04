package org.drools.model.builder;

import org.drools.model.Expression;
import org.drools.model.builder.impl.IndexBuilder;

public interface JoinBuilder {
    JoinBuilder rhsVar(String var);
    //IndexBuilder<JoinBuilder> index();
    JoinBuilder filter(Expression e);
}
