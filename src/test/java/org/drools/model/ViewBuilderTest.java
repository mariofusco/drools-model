package org.drools.model;

import org.junit.Test;

import static org.drools.model.builder.impl.FromBuilderImpl.from;
import static org.drools.model.impl.JavaClassType.typeOf;

public class ViewBuilderTest {

    @Test
    public void test() {
        // Person(name == "Mark", age > 18) from entry-point "persons"

        DataSource persons = null; // TODO

        from(typeOf(Person.class))
                .source(persons)
                .index().equalTo((Person p) -> p.getName(), () -> "Mark")
                .index().equalTo(null);
    }
}
