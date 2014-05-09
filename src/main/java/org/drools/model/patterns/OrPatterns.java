package org.drools.model.patterns;

import org.drools.model.Pattern;

import java.util.Arrays;
import java.util.List;

public class OrPatterns implements Pattern {

    private final List<Pattern> patterns;

    public OrPatterns(Pattern... patterns) {
        this.patterns = Arrays.asList(patterns);
    }

    @Override
    public List<Pattern> getPatterns() {
        return patterns;
    }

    @Override
    public Type getType() {
        return Type.OR;
    }
}