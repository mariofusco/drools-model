package org.drools.model;

import org.junit.Test;

import static org.drools.model.builder.impl.FromBuilderImpl.from;
import static org.drools.model.impl.CollectionObjectSource.sourceOf;
import static org.drools.model.impl.DataSourceImpl.dataSource;
import static org.drools.model.impl.JavaClassType.typeOf;

public class ViewBuilderTest {

    @Test
    public void testSimpleFrom() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40)));

        // Person(name == "Mark", age > 18) from entry-point "persons"

        FromModel<Person> from1 =
                from(typeOf(Person.class))
                        .source(persons)
                        .index().equalTo(Person::getName, () -> "Mark")
                        .index().greaterThan(Person::getAge, () -> 18)
                        .build();
    }

    @Test
    public void testJoin() {

        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40)));

        // $mark: Person(name == "Mark") from entry-point "persons"
        // Person(age > $mark.age) from entry-point "persons"

        FromModel<Person> from1 =
                from(typeOf(Person.class))
                        .source(persons)
                        .index().equalTo(Person::getName, () -> "Mark")
                        .build();

        FromModel<Person> from2 =
                from(typeOf(Person.class))
                        .source(persons)
                        .index().greaterThan(Person::getAge, () -> from1.reference().getAge()) // join
                        .build();
    }
}
