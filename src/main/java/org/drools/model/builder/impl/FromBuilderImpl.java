package org.drools.model.builder.impl;

import org.drools.model.DataSource;
import org.drools.model.IndexModel;
import org.drools.model.Type;
import org.drools.model.builder.FromBuilder;
import org.drools.model.builder.Indexable;

public class FromBuilderImpl implements FromBuilder {

    private final Type type;

    private FromBuilderImpl(Type type) {
        this.type = type;
    }

    public WithSource source(DataSource dataSource) {
        return new WithSourceImpl(type, dataSource);
    }

    //IndexBuilder<FromBuilderImpl> index();
    //FromBuilderImpl filter(Expression e);

    public static FromBuilderImpl from(Type type) {
        return new FromBuilderImpl(type);
    }

    public static class WithSourceImpl implements FromBuilder.WithSource, Indexable {
        private final Type type;
        private final DataSource dataSource;

        public WithSourceImpl(Type type, DataSource dataSource) {
            this.type = type;
            this.dataSource = dataSource;
        }

        public IndexBuilder<WithSourceImpl> index() {
            return new IndexBuilder<WithSourceImpl>(this);
        }

        @Override
        public void addIndex(IndexModel index) {
            throw new UnsupportedOperationException("org.drools.model.builder.impl.FromBuilderImpl.WithSourceImpl.addIndex -> TODO");

        }
    }
}
