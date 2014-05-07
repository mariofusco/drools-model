package org.drools.model.impl;

import org.drools.model.Consequence;
import org.drools.model.Rule;
import org.drools.model.View;

public class RuleImpl implements Rule {
    private final View view;
    private final Consequence consequence;

    public RuleImpl(View view, Consequence consequence) {
        this.view = view;
        this.consequence = consequence;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public Consequence getConsequence() {
        return consequence;
    }
}
