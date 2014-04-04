package org.drools.model.builder;

import org.drools.model.DataSource;
import org.drools.model.Expression;
import org.drools.model.builder.impl.FromBuilderImpl;

public interface ViewBuilder {
    FromBuilderImpl from(DataSource ds);
    FromBuilderImpl from(DataSource ds, String name);
    void join();

    public static interface group {
        //and()
    }

    public static class From {
        public static FromBuilderImpl source(DataSource ds) {
            return null;
        }
    }



    public static interface NotBuilder extends JoinBuilder{

    }

    public static interface ExistsBuilder extends JoinBuilder {

    }


    public static class ExpressionSelf<T> implements Expression {
        public T eval(T t) {
            return t;
        }
    }

    public static interface ExpressionO1<T, P1> extends Expression{
        T eval(P1 p1);
    }

    public static interface ExpressionO2<T, P1,P2> extends Expression {
        T eval(P1 p1, P2 p2);
    }

    public static interface ExpressionO3<T, P1, P2, P3> extends Expression {
        T eval(P1 p1, P2 p2, P3 p3);
    }

    public static interface ExpressionL1<P1> extends Expression{
        long eval(P1 p1);
    }

    public static interface ExpressionL2<P1, P2> extends Expression{
        long eval(P1 p1, P2 p2);
    }

    public static interface ExpressionL3<P1, P2, P3> extends Expression{
        long eval(P1 p1, P2 p2, P3 p3);
    }

    public static interface ExpressionD1<P1> extends Expression{
        double eval(P1 p1);
    }

    public static interface ExpressionB<P1> extends Expression{
        boolean eval(P1 p1);
    }

    public static interface Index {
        public Object getValues();
    }

}
