package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import java.util.List;

import static org.drools.model.DSL.typeOf;
import static org.drools.model.DSL.bind;
import static org.drools.model.flow.FlowDSL.*;
import static org.drools.model.impl.DataSourceImpl.sourceOf;
import static org.junit.Assert.assertEquals;

public class FlowDSLTest {

    @Test
    public void testJoin() {

        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

        // $mark: Person(name == "Mark") from entry-point "persons"
        // $older: Person(name != "Mark" && age > $mark.age) from entry-point "persons"

        Variable<Person> markV = bind(typeOf(Person.class));
        Variable<Person> olderV = bind(typeOf(Person.class));

        View view = view(
            input(markV, () -> persons),
            input(olderV, () -> persons),
            expr(markV, mark -> mark.getName().equals("Mark")),
            expr(olderV, older -> !older.getName().equals("Mark")),
            expr(olderV, markV, (older, mark) -> older.getAge() > mark.getAge())
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(markV).getName());
        assertEquals("Mario", tuple.get(olderV).getName());
    }

    @Test
    public void testJoinDifferentConstraintOrder() {

        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

        // $mark: Person(name == "Mark") from entry-point "persons"
        // $older: Person(name != "Mark" && age > $mark.age) from entry-point "persons"

        Variable<Person> markV = bind(typeOf(Person.class));
        Variable<Person> olderV = bind(typeOf(Person.class));

        View view = view(
            input(markV, () -> persons),
            expr(markV, mark -> mark.getName().equals("Mark")),
            input(olderV, () -> persons),
            expr(markV, olderV, (mark, older) -> mark.getAge() < older.getAge()),
            expr(olderV, older -> !older.getName().equals("Mark"))
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(markV).getName());
        assertEquals("Mario", tuple.get(olderV).getName());
    }

    @Test
    public void testOr() {
        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

        Variable<Person> markV = bind(typeOf(Person.class));
        Variable<Person> otherV = bind(typeOf(Person.class));

        View view = view(
                input(markV, () -> persons),
                input(otherV, () -> persons),
                expr(markV, mark -> mark.getName().equals("Mark")),
                or( expr(otherV, markV, (other, mark) -> other.getAge() > mark.getAge()),
                    expr(otherV, markV, (other, mark) -> other.getName().compareToIgnoreCase(mark.getName()) > 0)
                )
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(2, result.size());

        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(markV).getName());
        assertEquals("Mario", tuple.get(otherV).getName());

        tuple = result.get(1);
        assertEquals("Mark", tuple.get(markV).getName());
        assertEquals("Sofia", tuple.get(otherV).getName());
    }
}