package org.drools.model.engine;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.LHS;
import org.drools.model.Pattern;
import org.drools.model.SingleConstraint;
import org.drools.model.TupleHandle;
import org.drools.model.Type;
import org.drools.model.Variable;
import org.drools.model.constraints.AndConstraints;
import org.drools.model.constraints.OrConstraints;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.impl.TupleHandleImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BruteForceEngine {

    private static final BruteForceEngine INSTANCE = new BruteForceEngine();

    public static BruteForceEngine get() {
        return INSTANCE;
    }

    public List<TupleHandle> evaluate(LHS lhs) {
        return lhs.getPatterns().stream()
                .reduce(new Bindings(),
                        (bindings, pattern) -> evaluatePattern(pattern, bindings),
                        (b1, b2) -> null)
                .toTupleHandles();
    }

    public List<TupleHandle> evaluate(Pattern pattern) {
        return evaluatePattern(pattern, new Bindings()).toTupleHandles();
    }

    private Bindings evaluatePattern(Pattern pattern, Bindings bindings) {
        Stream<Object> objects = getObjectsOfType(pattern.getDataSource(), pattern.getVariable().getType());
        List<BoundTuple> tuples =
                objects.flatMap(obj -> generateMatches(pattern, bindings, obj))
                       .collect(toList());
        return new Bindings(tuples);
    }

    private Stream<Object> getObjectsOfType(DataSource dataSource, Type type) {
        return dataSource.getObjectSources().parallelStream()
                .flatMap(source -> source.getObjects().stream())
                .filter(type::isInstance);
    }

    private Stream<BoundTuple> generateMatches(Pattern pattern, Bindings bindings, Object obj) {
        return bindings.tuples.parallelStream()
                        .map(t -> t.bind(pattern.getVariable(), obj))
                        .filter(t -> match(pattern.getConstraint(), t.getTupleHandle()));
    }

    private boolean match(Constraint constraint, TupleHandle tuple) {
        switch (constraint.getType()) {
            case SINGLE:
                Variable[] vars = ((SingleConstraint)constraint).getVariables();
                switch (vars.length) {
                    case 1:
                        Object obj = tuple.get(vars[0]);
                        SingleConstraint1 singleCon = (SingleConstraint1)constraint;
                        return singleCon.getPredicate().test(obj);
                    case 2:
                        Object obj1 = tuple.get(vars[0]);
                        Object obj2 = tuple.get(vars[1]);
                        SingleConstraint2 singleCon2 = (SingleConstraint2)constraint;
                        return singleCon2.getPredicate().test(obj1, obj2);
                }
            case AND:
                AndConstraints andCon = (AndConstraints)constraint;
                return andCon.getChildren().stream().allMatch(con -> match(con, tuple));
            case OR:
                OrConstraints orCon = (OrConstraints)constraint;
                return orCon.getChildren().stream().anyMatch(con -> match(con, tuple));
        }
        return false;
    }

    private static class Bindings {
        private final List<BoundTuple> tuples;

        Bindings() {
            tuples = new ArrayList<>();
            tuples.add(new BoundTuple());
        }

        Bindings(List<BoundTuple> tuples) {
            this.tuples = tuples;
        }

        List<TupleHandle> toTupleHandles() {
            return tuples.parallelStream()
                    .map(BoundTuple::getTupleHandle)
                    .collect(toList());
        }

        @Override
        public String toString() {
            return tuples.toString();
        }
    }

    private static class BoundTuple {
        private final TupleHandle tuple;

        BoundTuple() {
            this(null);
        }

        BoundTuple(TupleHandleImpl tuple) {
            this.tuple = tuple;
        }

        BoundTuple bind(Variable var, Object obj) {
            return new BoundTuple(new TupleHandleImpl(tuple, obj, var));
        }

        TupleHandle getTupleHandle() {
            return tuple;
        }

        @Override
        public String toString() {
            return tuple.toString();
        }
    }
}
