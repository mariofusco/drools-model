package org.drools.model;

public interface Rule {

    // TODO: should it have (dynamic) salience? agenda-group? which other attributes?

    View getView();

    Consequence getConsequence();
}
