package org.drools.model.impl;

import org.drools.model.Pattern;

public class PatternArray implements Pattern {
    private final Pattern[] patterns;

    public PatternArray(Pattern... patterns) {
        this.patterns = patterns;
    }
}
