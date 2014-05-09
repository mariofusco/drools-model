package org.drools.model.patterns;

import org.drools.model.Pattern;
import org.drools.model.View;

import java.util.Arrays;
import java.util.List;

public class AndPatterns implements Pattern, View {

    private final List<Pattern> patterns;

    public AndPatterns(Pattern... patterns) {
        this.patterns = Arrays.asList(patterns);
    }

    @Override
    public List<Pattern> getPatterns() {
        return patterns;
    }

    @Override
    public Type getType() {
        return Type.AND;
    }
}
