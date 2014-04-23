package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.drools.model.DSL.*;
import static org.drools.model.DSL.lhs;
import static org.drools.model.impl.CollectionObjectSource.sourceOf;
import static org.drools.model.impl.DataSourceImpl.dataSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewBuilderTest {

    @Test
    public void testSimpleFrom() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40),
                                                 new Person("Sofia", 3)));

        // Person(name == "Mark" or (age > 18 and age < 65)) from entry-point "persons"

        Pattern pattern =
                filter(typeOf(Person.class))
                        .with(person -> person.getName().equals("Mark"))
                        .or(person -> person.getAge() > 18 && person.getAge() < 65)
                        .from(persons);

        List<TupleHandle> result = BruteForceEngine.get().evaluate(pattern);
        assertEquals(3, result.size());
        List<String> names = result.stream()
                                .map(TupleHandle::getObject)
                                .map(o -> ((Person) o).getName())
                                .collect(toList());
        assertTrue(names.containsAll(asList("Mark", "Edson", "Mario")));
    }

    @Test
    public void testJoin() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40)));

        // $mark: Person(name == "Mark") from entry-point "persons"
        // $older: Person(name != "Mark" && age > $mark.age) from entry-point "persons"

        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> older = bind(typeOf(Person.class));
        LHS patterns = lhs(
                filter(mark)
                        .with(person -> person.getName().equals("Mark"))
                        .from(persons),
                using(mark).filter(older)
                        .with(person -> !person.getName().equals("Mark"))
                        .and(older, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                        .from(persons)
                          );

        System.out.println(patterns);
    }
}
