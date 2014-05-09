package org.drools.model.patterns;

import org.drools.model.Pattern;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSinglePattern {

    public List<Pattern> getPatterns() {
        return Collections.emptyList();
    }

    public Pattern.Type getType() {
        return Pattern.Type.SINGLE;
    }
}
