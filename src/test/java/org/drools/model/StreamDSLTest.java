package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import java.util.List;

import static org.drools.model.DSL.typeOf;
import static org.drools.model.DSL.bind;
import static org.drools.model.stream.StreamDSL.*;
import static org.drools.model.impl.DataSourceImpl.sourceOf;
import static org.junit.Assert.assertEquals;

public class StreamDSLTest {

    @Test
    public void testJoin() {

        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

        // $mark: Person(name == "Mark") from entry-point "persons"
        // $older: Person(name != "Mark" && age > $mark.age) from entry-point "persons"

        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> older = bind(typeOf(Person.class));

        View view = view(
            input(mark, () -> persons),
            input(older, () -> persons),
            expr(mark, p -> p.getName().equals("Mark")),
            expr(older, p -> !p.getName().equals("Mark")),
            expr(older, mark, (p1, p2) -> p1.getAge() > p2.getAge())
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(mark).getName());
        assertEquals("Mario", tuple.get(older).getName());
    }
}
