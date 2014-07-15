package org.drools.model.impl;

import org.drools.model.Consequence;
import org.drools.model.Rule;
import org.drools.model.View;

import java.util.Map;

public class RuleImpl implements Rule {

    private final String name;
    private final View view;
    private final Consequence consequence;

    private Map<Attribute, Object> attributes;

    public RuleImpl(String name, View view, Consequence consequence) {
        this.name = name;
        this.view = view;
        this.consequence = consequence;
    }

    public RuleImpl(String name, View view, Consequence consequence, Map<Attribute, Object> attributes) {
        this(name, view, consequence);
        this.attributes = attributes;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public Consequence getConsequence() {
        return consequence;
    }

    @Override
    public Object getAttribute(Attribute attribute) {
        Object value = attributes != null ? attributes.get(attribute) : null;
        return value != null ? value : attribute.getDefaultValue();
    }

    @Override
    public String getName() {
        return name;
    }
}
