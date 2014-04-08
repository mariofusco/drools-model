package org.drools.model;

import java.util.List;

// equivalent to OTN
public interface FromModel<T> {
    Type<T> getType();
    DataSource getDataSource();

    List<Processor> getProcessors();

    T reference();

    //Expression getIndex();
    //Expression getExpression();
}
