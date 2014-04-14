package org.drools.model;

import org.junit.Test;

import static org.drools.model.DSL.*;
import static org.drools.model.impl.CollectionObjectSource.sourceOf;
import static org.drools.model.impl.DataSourceImpl.dataSource;

public class ViewBuilderTest {

    @Test
    public void testSimpleFrom() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40)));

        // Person(name == "Mark" or (age > 18 and age < 65)) from entry-point "persons"

        Pattern pattern =
                filter(typeOf(Person.class))
                        .with(person -> person.getName().equals("Mark"))
                        .or(person -> person.getAge() > 18 && person.getAge() < 65)
                        .from(persons);
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
        Pattern pattern = patterns(
                filter(mark)
                        .with(person -> person.getName().equals("Mark"))
                        .from(persons),
                using(mark).filter(older)
                        .with(person -> !person.getName().equals("Mark"))
                        .and(older, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                        .from(persons)
        );
    }
}
