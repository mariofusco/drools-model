package org.drools.model.engine;

import org.drools.model.AccumulateFunction;
import org.drools.model.AccumulatePattern;
import org.drools.model.Condition;
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
import org.drools.model.impl.TupleHandleImpl;

import java.io.Serializable;
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

    public void evaluate(Rule... rules) {
        List<Rule> firedRules = stream(rules).filter(rule -> {
            List<TupleHandle> matches = evaluate(rule.getView());
            matches.forEach(match -> {
                rule.getConsequence().getBlock().execute(
                        stream(rule.getConsequence().getDeclarations()).map(match::get).toArray());
            });
            return !matches.isEmpty();
        }).collect(toList());

        // TODO: implement conflict resulution strategy (?)

        if ( firedRules.stream()
                       .filter(rule -> rule.getConsequence().isChangingWorkingMemory())
                       .findFirst().isPresent() ) {
            evaluate(rules);
        }
    }

    public List<TupleHandle> evaluate(View view) {
        return evaluateCondition(view, initialBindings()).toTupleHandles();
    }

    public List<TupleHandle> evaluate(Condition condition) {
        return evaluateCondition(condition, initialBindings()).toTupleHandles();
    }

    private Bindings evaluateCondition(Condition condition, Bindings bindings) {
        if (condition.getType() instanceof Condition.SingleType) {
            return evaluateSinglePattern((Pattern)condition, bindings);
        } else if (condition.getType() instanceof Condition.AndType) {
            return condition.getSubConditions().stream()
                            .reduce(bindings,
                                    (b, p) -> evaluateCondition(p, b),
                                    (b1, b2) -> null);
        } else if (condition.getType() instanceof Condition.OrType) {
            return condition.getSubConditions().stream()
                            .reduce(new Bindings(),
                                    (b, p) -> b.append(evaluateCondition(p, bindings)),
                                    (b1, b2) -> null);
        }
        return null;
    }

    private Bindings evaluateSinglePattern(Pattern pattern, Bindings bindings) {
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
        private final Serializable[] accumulators;

        private AccumulateReducer(AccumulatePattern pattern) {
            this.functions = pattern.getFunctions();
            this.accumulators = (Serializable[]) stream(functions).map(AccumulateFunction::init).toArray();
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
        return dataSource.getObjects().parallelStream()
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
                SingleConstraint singleCon = (SingleConstraint)constraint;
                Variable[] vars = singleCon.getVariables();
                switch (vars.length) {
                    case 1:
                        Object obj = tuple.get(vars[0]);
                        return singleCon.getPredicate().test(obj);
                    case 2:
                        Object obj1 = tuple.get(vars[0]);
                        Object obj2 = tuple.get(vars[1]);
                        return singleCon.getPredicate().test(obj1, obj2);
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
        }

        Bindings(List<BoundTuple> tuples) {
            this.tuples = tuples;
        }

        List<TupleHandle> toTupleHandles() {
            return tuples.parallelStream()
                    .map(BoundTuple::getTupleHandle)
                    .collect(toList());
        }

        public Bindings append(Bindings other) {
            return new Bindings(new ArrayList<BoundTuple>() {{
                addAll(tuples);
                addAll(other.tuples);
            }});
        }

        @Override
        public String toString() {
            return tuples.toString();
        }

    }

    static Bindings initialBindings() {
        Bindings bindings = new Bindings();
        bindings.tuples.add(new BoundTuple());
        return bindings;
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
