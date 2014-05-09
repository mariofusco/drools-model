package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.drools.model.DSL.*;
import static org.drools.model.DSL.filter;
import static org.drools.model.functions.accumulate.Average.avg;
import static org.drools.model.functions.accumulate.Reduce.reduce;
import static org.drools.model.functions.accumulate.Sum.sum;
import static org.drools.model.impl.CollectionObjectSource.sourceOf;
import static org.drools.model.impl.DataSourceImpl.dataSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewBuilderTest {

    @Test
    public void testSimpleView() {

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
        View view = view(
                filter(mark)
                        .with(person -> person.getName().equals("Mark"))
                        .from(persons),
                using(mark).filter(older)
                        .with(person -> !person.getName().equals("Mark"))
                        .and(older, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                        .from(persons)
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(mark).getName());
        assertEquals("Mario", tuple.get(older).getName());
    }

    @Test
    public void testNot() {
        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40)));

        // $oldest: Person()
        // not( Person(age > $oldest.age) )

        Variable<Person> oldest = bind(typeOf(Person.class));

        View view = view(
                filter(oldest).from(persons),
                not( using(oldest).filter(typeOf(Person.class))
                             .with(oldest, (p1, p2) -> p1.getAge() > p2.getAge())
                             .from(persons) )
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mario", tuple.get(oldest).getName());
    }

    @Test
    public void testExists() {
        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40)));

        // $person: Person()
        // exists( Person(name.length > $person.name.length) )

        Variable<Person> person = bind(typeOf(Person.class));

        View view = view(
                filter(person).from(persons),
                exists( using(person).filter(typeOf(Person.class))
                             .with(person, (p1, p2) -> p1.getName().length() > p2.getName().length())
                             .from(persons) )
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(person).getName());
    }

    @Test
    public void testAccumulate() {
        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40)));

        // accumulate( $p : Person(name.startsWith("M"));
        //             $sum : sum( $p.age ),
        //             $avg : avg( $p.age ),
        //             $max : reduce( init( int max = 0; ), // attempt of a generic accumulate
        //                            action( person.getAge() > max ? person.getAge() : max )
        //                          )
        //           )

        Variable<Integer> resultSum = bind(typeOf(Integer.class));
        Variable<Double> resultAvg = bind(typeOf(Double.class));
        Variable<Integer> resultMax = bind(typeOf(Integer.class));

        View view = view(
                accumulate( filter(typeOf(Person.class))
                                    .with(person -> person.getName().startsWith("M"))
                                    .from(persons),
                            sum(Person::getAge).as(resultSum),
                            avg(Person::getAge).as(resultAvg),
                            reduce(0, (Integer max, Person p) -> p.getAge() > max ? p.getAge() : max).as(resultMax) )
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals(77, (int)tuple.get(resultSum));
        assertEquals(38.5, (double)tuple.get(resultAvg), 0.01);
        assertEquals(40, (int)tuple.get(resultMax), 0.01);
    }

    @Test
    public void testOr() {
        DataSource persons = dataSource(sourceOf(new Person("Mark", 37),
                                                 new Person("Edson", 35),
                                                 new Person("Mario", 40),
                                                 new Person("Sofia", 3)));

        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> other = bind(typeOf(Person.class));

        View view = view(
                filter(mark)
                        .with(person -> person.getName().equals("Mark"))
                        .from(persons),
                or(
                        using(mark).filter(other)
                                .with(other, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                                .from(persons),
                        using(mark).filter(other)
                                .with(other, mark, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()) > 0)
                                .from(persons)
                ));


        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(2, result.size());

        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(mark).getName());
        assertEquals("Mario", tuple.get(other).getName());

        tuple = result.get(1);
        assertEquals("Mark", tuple.get(mark).getName());
        assertEquals("Sofia", tuple.get(other).getName());
    }
}
