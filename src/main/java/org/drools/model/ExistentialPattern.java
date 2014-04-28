package org.drools.model;

public interface ExistentialPattern extends JoinPattern {
    enum ExistentialType { NOT, EXISTS }

    ExistentialType getExistentialType();
}
