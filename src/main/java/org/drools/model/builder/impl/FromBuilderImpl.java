package org.drools.model.builder.impl;

import org.drools.model.*;
import org.drools.model.builder.FromBuilder;
import org.drools.model.builder.Indexable;
import org.drools.model.impl.*;

import java.util.*;

public class FromBuilderImpl<T> implements FromBuilder {

    private final Type<T> type;

    private FromBuilderImpl(Type<T> type) {
        this.type = type;
    }

    public WithSource<T> source(DataSource dataSource) {
        return new WithSourceImpl<T>(type, dataSource);
    }

    //IndexBuilder<FromBuilderImpl> index();
    //FromBuilderImpl filter(Expression e);

    public static <T> FromBuilderImpl<T> from(Type<T> type) {
        return new FromBuilderImpl(type);
    }

    public static class WithSourceImpl<T> implements FromBuilder.WithSource<T>, Indexable {
        private final Type<T> type;
        private final DataSource dataSource;
        private final List<IndexModel> indexes = new ArrayList<IndexModel>();

        public WithSourceImpl(Type<T> type, DataSource dataSource) {
            this.type = type;
            this.dataSource = dataSource;
        }

        @Override
        public FromModel build() {
            return new FromModelImpl(type, dataSource, indexes);
        }

        @Override
        public IndexBuilder<T, WithSourceImpl<T>> index() {
            return new IndexBuilder<T, WithSourceImpl<T>>(this);
        }

        @Override
        public void addIndex(IndexModel index) {
            indexes.add(index);
        }
    }
}
