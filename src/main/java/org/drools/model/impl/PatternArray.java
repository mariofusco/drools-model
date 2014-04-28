package org.drools.model.impl;

import org.drools.model.View;
import org.drools.model.Pattern;

import java.util.Arrays;
import java.util.List;

public class PatternArray implements View {
    private final List<Pattern> patterns;

    public PatternArray(Pattern... patterns) {
        this.patterns = Arrays.asList(patterns);
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }
}
