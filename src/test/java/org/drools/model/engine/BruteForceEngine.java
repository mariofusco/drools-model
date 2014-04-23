package org.drools.model.engine;

import org.drools.model.Constraint;
import org.drools.model.DataSource;
import org.drools.model.Pattern;
import org.drools.model.SingleConstraint;
import org.drools.model.TupleHandle;
import org.drools.model.Type;
import org.drools.model.constraints.AndConstraints;
import org.drools.model.constraints.OrConstraints;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.impl.TupleHandleImpl;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BruteForceEngine {

    private static final BruteForceEngine INSTANCE = new BruteForceEngine();

    public static BruteForceEngine get() {
        return INSTANCE;
    }

    public List<TupleHandle> evaluate(Pattern pattern) {
        if (pattern.getType().equals(Pattern.Type.SIMPLE)) {
            return evaluateSimple(pattern);
        }
        return null;
    }

    public List<TupleHandle> evaluateSimple(Pattern pattern) {
        Stream<Object> objects = getObjectsOfType(pattern.getDataSource(), pattern.getVariable().getType());
        return objects.filter(obj -> match(pattern.getConstraint(), obj))
                .map(obj -> new TupleHandleImpl(obj, pattern.getVariable()))
                .collect(toList());
    }

    private Stream<Object> getObjectsOfType(DataSource dataSource, Type type) {
        return dataSource.getObjectSources().parallelStream()
                .flatMap(source -> source.getObjects().stream())
                .filter(type::isInstance);
    }

    private boolean match(Constraint constraint, Object obj) {
        switch (constraint.getType()) {
            case SINGLE:
                switch (((SingleConstraint)constraint).getVariables().length) {
                    case 1:
                        SingleConstraint1 singleCon = (SingleConstraint1)constraint;
                        return singleCon.getPredicate().test(obj);
                }
                break;
            case AND:
                AndConstraints andCon = (AndConstraints)constraint;
                return andCon.getChildren().stream().allMatch(con -> match(con, obj));
            case OR:
                OrConstraints orCon = (OrConstraints)constraint;
                return orCon.getChildren().stream().anyMatch(con -> match(con, obj));
        }
        return false;
    }
}
