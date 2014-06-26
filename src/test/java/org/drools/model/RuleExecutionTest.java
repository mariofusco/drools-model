package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.drools.model.DSL.*;
import static org.drools.model.impl.DataSourceImpl.sourceOf;
import static org.drools.model.impl.DataStoreImpl.storeOf;
import static org.junit.Assert.assertEquals;

public class RuleExecutionTest {

    // TODO: add eval
    // TODO: implement no LHS with eval(true)
    // TODO: globals

    @Test
    public void testSimpleRule() {

        DataSource persons = sourceOf(new Person("Mark", 37),
                                      new Person("Edson", 35),
                                      new Person("Mario", 40),
                                      new Person("Sofia", 3));

        List<String> list = new ArrayList<>();
        Variable<Person> mark = bind(typeOf(Person.class));

        Rule rule = rule(
                attributes().set(Rule.Attribute.SALIENCE, 10)
                        .set(Rule.Attribute.AGENDA_GROUP, "myGroup"),
                view(p -> p.filter(mark)
                           .with(person -> person.getName().equals("Mark"))
                           .from(persons)),
                then(c -> c.on(mark)
                           .execute(p -> list.add(p.getName())))
        );

        BruteForceEngine.get().evaluate(rule);
        assertEquals(1, list.size());
        assertEquals("Mark", list.get(0));

        assertEquals(10, rule.getAttribute(Rule.Attribute.SALIENCE));
        assertEquals("myGroup", rule.getAttribute(Rule.Attribute.AGENDA_GROUP));
        assertEquals(false, rule.getAttribute(Rule.Attribute.NO_LOOP));
    }

    @Test
    public void testJoin() {

        DataSource persons = sourceOf(new Person("Mark", 37),
                                      new Person("Edson", 35),
                                      new Person("Mario", 40),
                                      new Person("Sofia", 3));

        List<String> list = new ArrayList<>();
        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> older = bind(typeOf(Person.class));

        Rule rule = rule(
                view(
                        p -> p.filter(mark)
                                .with(person -> person.getName().equals("Mark"))
                                .from(persons),
                        p -> p.filter(older)
                                .with(person -> !person.getName().equals("Mark"))
                                .and(older, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                                .from(persons)
                    ),
            then(c -> c.on(older, mark)
                       .execute((p1, p2) -> list.add(p1.getName() + " is older than " + p2.getName())))
        );

        BruteForceEngine.get().evaluate(rule);
        assertEquals(1, list.size());
        assertEquals("Mario is older than Mark", list.get(0));
    }

    @Test
    public void testDelete() {
        DataStore persons = storeOf(new Person("Mark", 37),
                                    new Person("Edson", 35),
                                    new Person("Mario", 40),
                                    new Person("Sofia", 3));

        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> younger = bind(typeOf(Person.class));

        Rule rule = rule(
                view(
                        p -> p.filter(mark)
                              .with(person -> person.getName().equals("Mark"))
                              .from(persons),
                        p -> p.filter(younger)
                              .with(person -> !person.getName().equals("Mark"))
                              .and(younger, mark, (p1, p2) -> p1.getAge() < p2.getAge())
                              .from(persons)
                    ),
                then(c -> c.on(younger)
                           .execute(y -> persons.delete(y))
                           .deletes(younger))
        );

        BruteForceEngine.get().evaluate(rule);
        assertEquals(2, persons.getObjects().size());
    }

    @Test
    public void testUpdate() {
        Person mario = new Person("Mario", 40);

        DataStore<Object> persons = storeOf(new Person("Mark", 37),
                                            new Person("Edson", 35),
                                            mario,
                                            new Person("Sofia", 3));

        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> other = bind(typeOf(Person.class));

        Rule rule = rule(
                view(
                        p -> p.filter(mark)
                              .with(person -> person.getName().equals("Mark"))
                              .from(persons),
                        p -> p.filter(other)
                              .with(person -> !person.getName().equals("Mark"))
                              .and(other, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                              .from(persons)
                    ),
                then(c -> c.on(other)
                           .execute(o -> persons.update(o, p -> p.setAge(p.getAge()-1)))
                           .updates(other, "age"))
        );

        BruteForceEngine.get().evaluate(rule);
        assertEquals(37, mario.getAge());
    }
}
