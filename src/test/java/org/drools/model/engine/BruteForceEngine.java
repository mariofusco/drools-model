package org.drools.model.engine;

import org.drools.model.AccumulateFunction;
import org.drools.model.AccumulatePattern;
import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.ExistentialPattern;
import org.drools.model.Pattern;
import org.drools.model.Rule;
import org.drools.model.SingleConstraint;
import org.drools.model.TupleHandle;
import org.drools.model.Type;
import org.drools.model.Variable;
import org.drools.model.View;
import org.drools.model.constraints.AndConstraints;
import org.drools.model.constraints.OrConstraints;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.impl.TupleHandleImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class BruteForceEngine {

    private static final BruteForceEngine INSTANCE = new BruteForceEngine();

    public static BruteForceEngine get() {
        return INSTANCE;
    }

    public void evaluate(Rule rule) {
        List<TupleHandle> matches = evaluate(rule.getView());
        matches.forEach(match -> {
            rule.getConsequence().getBlock().execute(
                    stream(rule.getConsequence().getVariables()).map(match::get).toArray());
        });
    }

    public List<TupleHandle> evaluate(View view) {
        return view.getPatterns().stream()
                .reduce(new Bindings(),
                        (bindings, pattern) -> evaluatePattern(pattern, bindings),
                        (b1, b2) -> null)
                .toTupleHandles();
    }

    public List<TupleHandle> evaluate(Pattern pattern) {
        return evaluatePattern(pattern, new Bindings()).toTupleHandles();
    }

    private Bindings evaluatePattern(Pattern pattern, Bindings bindings) {
        if (pattern instanceof ExistentialPattern) {
            return evaluateExistential((ExistentialPattern) pattern, bindings);
        }
        if (pattern instanceof AccumulatePattern) {
            return evaluateAccumulate((AccumulatePattern) pattern, bindings);
        }
        Stream<Object> objects = getObjectsOfType(pattern.getDataSource(), pattern.getVariable().getType());
        List<BoundTuple> tuples =
                objects.flatMap(obj -> generateMatches(pattern, bindings, obj))
                       .collect(toList());
        return new Bindings(tuples);
    }

    private Bindings evaluateExistential(ExistentialPattern pattern, Bindings bindings) {
        List<Object> objects = getObjectsOfType(pattern.getDataSource(), pattern.getVariable().getType()).collect(toList());
        Predicate<BoundTuple> existentialPredicate =
                tuple -> objects.stream()
                                 .map(obj -> tuple.bind(pattern.getVariable(), obj))
                                 .anyMatch(t -> match(pattern.getConstraint(), t));
        if (pattern.getExistentialType() == ExistentialPattern.ExistentialType.NOT) {
            existentialPredicate = existentialPredicate.negate();
        }
        List<BoundTuple> tuples =
            bindings.tuples.parallelStream()
                    .filter(existentialPredicate)
                    .collect(toList());
        return new Bindings(tuples);
    }

    private Bindings evaluateAccumulate(AccumulatePattern pattern, Bindings bindings) {
        List<Object> objects = getObjectsOfType(pattern.getDataSource(), pattern.getVariable().getType()).collect(toList());
        List<BoundTuple> tuples =
                bindings.tuples.parallelStream()
                        .map(tuple -> objects.stream()
                                             .filter(obj -> match(pattern.getConstraint(), tuple.bind(pattern.getVariable(), obj)))
                                             .reduce(new AccumulateReducer(pattern), AccumulateReducer::accumulate, (a1, a2) -> null)
                                             .bindAllResults(tuple))
                        .collect(toList());
        return new Bindings(tuples);
    }

    private static class AccumulateReducer {
        private final AccumulateFunction[] functions;
        private final Object[] accumulators;

        private AccumulateReducer(AccumulatePattern pattern) {
            this.functions = pattern.getFunctions();
            this.accumulators = stream(functions).map(AccumulateFunction::init).toArray();
        }

        public AccumulateReducer accumulate(Object obj) {
            for (int i = 0; i < functions.length; i++) {
                accumulators[i] = functions[i].action(accumulators[i], obj);
            }
            return this;
        }

        public BoundTuple bindAllResults(BoundTuple tuple) {
            for (int i = 0; i < functions.length; i++) {
                tuple = tuple.bind(functions[i].getVariable(), functions[i].result(accumulators[i]));
            }
            return tuple;
        }
    }

    private Stream<Object> getObjectsOfType(DataSource dataSource, Type type) {
        return dataSource.getObjectSources().parallelStream()
                .flatMap(source -> source.getObjects().stream())
                .filter(type::isInstance);
    }

    private Stream<BoundTuple> generateMatches(Pattern pattern, Bindings bindings, Object obj) {
        return bindings.tuples.parallelStream()
                        .map(t -> t.bind(pattern.getVariable(), obj))
                        .filter(t -> match(pattern.getConstraint(), t));
    }

    private boolean match(Constraint constraint, BoundTuple tuple) {
        return match(constraint, tuple.getTupleHandle());
    }
    
    private boolean match(Constraint constraint, TupleHandle tuple) {
        switch (constraint.getType()) {
            case TRUE:
                return true;
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
