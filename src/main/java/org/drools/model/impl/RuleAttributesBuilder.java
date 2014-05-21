package org.drools.model.impl;

import org.drools.model.Rule;

import java.util.HashMap;
import java.util.Map;

public class RuleAttributesBuilder {

    private final Map<Rule.Attribute, Object> attributes = new HashMap<Rule.Attribute, Object>();

    public RuleAttributesBuilder set(Rule.Attribute attribute, Object value) {
        attributes.put(attribute, value);
        return this;
    }

    public Map<Rule.Attribute, Object> get() {
        return attributes;
    }
}
