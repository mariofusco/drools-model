package org.drools.model.impl;

import org.drools.model.Consequence;
import org.drools.model.Rule;
import org.drools.model.View;

import java.util.Map;

public class RuleImpl implements Rule {

    private final View view;
    private final Consequence consequence;

    private Map<Attribute, Object> attributes;

    public RuleImpl(View view, Consequence consequence) {
        this.view = view;
        this.consequence = consequence;
    }

    public RuleImpl(View view, Consequence consequence, Map<Attribute, Object> attributes) {
        this(view, consequence);
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
}
