package org.drools.model;

import org.drools.model.engine.BruteForceEngine;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.drools.model.DSL.*;
import static org.drools.model.DSL.view;
import static org.drools.model.functions.accumulate.Average.avg;
import static org.drools.model.functions.accumulate.Reduce.reduce;
import static org.drools.model.functions.accumulate.Sum.sum;
import static org.drools.model.impl.DataSourceImpl.sourceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewBuilderTest {

    @Test
    public void testSimpleView() {

        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35));

        persons.insert(new Person("Mario", 40));
        persons.insert(new Person("Sofia", 3));

        // Person(name == "Mark" or (age > 18 and age < 65)) from entry-point "persons"

        View view =
                view(p -> p.filter(typeOf(Person.class))
                           .with(person -> person.getName().equals("Mark"))
                           .or(person -> person.getAge() > 18 && person.getAge() < 65)
                           .from(persons));

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);

        assertEquals(3, result.size());
        List<String> names = result.stream()
                                .map(TupleHandle::getObject)
                                .map(o -> ((Person) o).getName())
                                .collect(toList());
        assertTrue(names.containsAll(asList("Mark", "Edson", "Mario")));
    }

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
        View view = view(persons,
                         p -> p.filter(mark)
                               .with(person -> person.getName().equals("Mark"))
                               .indexedBy(Index.ConstraintType.EQUAL, Person::getName, "Mark"),
                         p -> p.using(mark).filter(older)
                               .with(person -> !person.getName().equals("Mark"))
                               .indexedBy(Index.ConstraintType.NOT_EQUAL, Person::getName, "Mark")
                               .and(older, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                               .indexedBy(Index.ConstraintType.GREATER_THAN, Person::getAge, Person::getAge)
                        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mark", tuple.get(mark).getName());
        assertEquals("Mario", tuple.get(older).getName());
        
        List<Pattern> patterns = view.getPatterns();
        assertEquals(2, patterns.size());

        Index index = ((SingleConstraint)((SinglePattern)patterns.get(0)).getConstraint()).getIndex();
        assertEquals(Index.ConstraintType.EQUAL, index.getConstraintType());
        assertEquals(Index.IndexType.ALPHA, index.getIndexType());
        assertEquals("Mark", index.getLeftOperandExtractor().apply(new Person("Mark", 37)));
        assertEquals("Mark", ((AlphaIndex)index).getRightValue());

        index = ((SingleConstraint)((SinglePattern)patterns.get(1)).getConstraint().getChildren().get(0)).getIndex();
        assertEquals(Index.ConstraintType.NOT_EQUAL, index.getConstraintType());
        assertEquals(Index.IndexType.ALPHA, index.getIndexType());
        assertEquals("Mark", index.getLeftOperandExtractor().apply(new Person("Mark", 37)));
        assertEquals("Mark", ((AlphaIndex)index).getRightValue());

        index = ((SingleConstraint)((SinglePattern)patterns.get(1)).getConstraint().getChildren().get(1)).getIndex();
        assertEquals(Index.ConstraintType.GREATER_THAN, index.getConstraintType());
        assertEquals(Index.IndexType.BETA, index.getIndexType());
        assertEquals(37, index.getLeftOperandExtractor().apply(new Person("Mark", 37)));
        assertEquals(37, ((BetaIndex)index).getRightOperandExtractor().apply(new Person("Mark", 37)));
    }

    @Test
    public void testNot() {
        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

        // $oldest: Person()
        // not( Person(age > $oldest.age) )

        Variable<Person> oldest = bind(typeOf(Person.class));

        View view = view(
                p -> p.filter(oldest).from(persons),
                not(p -> p.using(oldest).filter(typeOf(Person.class))
                          .with(oldest, (p1, p2) -> p1.getAge() > p2.getAge())
                          .from(persons))
        );

        List<TupleHandle> result = BruteForceEngine.get().evaluate(view);
        assertEquals(1, result.size());
        TupleHandle tuple = result.get(0);
        assertEquals("Mario", tuple.get(oldest).getName());
    }

    @Test
    public void testExists() {
        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

        // $person: Person()
        // exists( Person(name.length > $person.name.length) )

        Variable<Person> person = bind(typeOf(Person.class));

        View view = view(
                p -> p.filter(person).from(persons),
                exists( p -> p.using(person).filter(typeOf(Person.class))
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
        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

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
                accumulate( p -> p.filter(typeOf(Person.class))
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
        DataSource<Person> persons = sourceOf(new Person("Mark", 37),
                                              new Person("Edson", 35),
                                              new Person("Mario", 40),
                                              new Person("Sofia", 3));

        Variable<Person> mark = bind(typeOf(Person.class));
        Variable<Person> other = bind(typeOf(Person.class));

        View view = view(
                pattern(p -> p.filter(mark)
                              .with(person -> person.getName().equals("Mark"))
                              .from(persons)),
                or(
                        pattern(p -> p.using(mark).filter(other)
                                      .with(other, mark, (p1, p2) -> p1.getAge() > p2.getAge())
                                      .from(persons)),
                        pattern(p -> p.using(mark).filter(other)
                                      .with(other, mark, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()) > 0)
                                      .from(persons))
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
