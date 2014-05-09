package org.drools.model;

import java.util.List;

public interface Pattern {
    enum Type { SINGLE, OR, AND }

    List<Pattern> getPatterns();

    Type getType();
}
