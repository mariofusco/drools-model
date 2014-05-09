package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.drools.model.DSL.*;
import static org.drools.model.impl.CollectionObjectSource.sourceOf;
import static org.drools.model.impl.DataSourceImpl.dataSource;
import static org.junit.Assert.assertEquals;

public class RuleExecutionTest {

    @Test
    public void testSimpleRule() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40),
                                                 new Person("Sofia", 3)));

        List<String> list = new ArrayList<>();
        Variable<Person> mark = bind(typeOf(Person.class));

        Rule rule = rule(
            filter(mark)
                    .with(person -> person.getName().equals("Mark"))
                    .from(persons),
            then(mark, p -> list.add(p.getName()))
        );

        BruteForceEngine.get().evaluate(rule);
        assertEquals(1, list.size());
        assertEquals("Mark", list.get(0));
    }

    @Test
    public void testJoin() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40),
                                                 new Person("Sofia", 3)));

        List<String> list = new ArrayList<>();
        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> older = bind(typeOf(Person.class));

        Rule rule = rule(
                view(
                        filter(mark)
                                .with(person -> person.getName().equals("Mark"))
                                .from(persons),
                        using(mark).filter(older)
                                .with(person -> !person.getName().equals("Mark"))
                                .and(older, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                                .from(persons)
                    ),
            then(older, mark, (p1, p2) -> list.add(p1.getName() + " is older than " + p2.getName()))
        );

        BruteForceEngine.get().evaluate(rule);
        assertEquals(1, list.size());
        assertEquals("Mario is older than Mark", list.get(0));
    }
}
