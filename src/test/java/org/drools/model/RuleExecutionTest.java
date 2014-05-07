package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import static org.drools.model.DSL.*;
import static org.drools.model.impl.CollectionObjectSource.sourceOf;
import static org.drools.model.impl.DataSourceImpl.dataSource;

public class RuleExecutionTest {

    @Test
    public void testSimpleRule() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40),
                                                 new Person("Sofia", 3)));

        Variable<Person> mark = bind(typeOf(Person.class));

        Rule rule = rule(
            filter(mark)
                    .with(person -> person.getName().equals("Mark"))
                    .from(persons),
            then(mark, p -> System.out.println(p.getName()))
        );

        BruteForceEngine.get().evaluate(rule);
    }

    @Test
    public void testJoin() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40),
                                                 new Person("Sofia", 3)));

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
            then(older, mark, (p1, p2) -> System.out.println(p1.getName() + " is older than " + p2.getName()))
        );

        BruteForceEngine.get().evaluate(rule);
    }
}
