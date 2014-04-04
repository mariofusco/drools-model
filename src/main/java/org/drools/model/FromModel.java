package org.drools.model;

import java.util.List;

// equivalent to OTN
public interface FromModel {
    Type getType();
    DataSource getDataSource();

    List<Processor> getProcessors();

    //Expression getIndex();
    //Expression getExpression();
}
