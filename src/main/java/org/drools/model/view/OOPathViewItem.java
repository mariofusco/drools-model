/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.model.view;

import java.util.List;

import org.drools.model.Source;
import org.drools.model.Variable;
import org.drools.model.functions.Function1;
import org.drools.model.functions.Predicate1;

public class OOPathViewItem<T> implements ViewItem<T> {
    private final Variable<T> variable;
    private final List<OOPathChunk<?>> chunks;

    public OOPathViewItem( Variable<T> variable, List<OOPathChunk<?>> chunks ) {
        this.variable = variable;
        this.chunks = chunks;
    }

    @Override
    public Variable<T> getFirstVariable() {
        return variable;
    }

    public List<OOPathChunk<?>> getChunks() {
        return chunks;
    }

    public abstract static class OOPathChunk<S> {
        private Predicate1<S> filter;

        public Predicate1<S> getFilter() {
            return filter;
        }

        public void setFilter( Predicate1<S> filter ) {
            this.filter = filter;
        }
    }

    public static class OOPathHeadChunk<S> extends OOPathChunk<S> {
        private final Source<S> source;

        public OOPathHeadChunk( Source<S> source ) {
            this.source = source;
        }

        public Source<S> getSource() {
            return source;
        }
    }

    public static class OOPathTailChunk<R, S> extends OOPathChunk<S> {
        private final Function1<R, S> map;

        public OOPathTailChunk( Function1<R, S> map ) {
            this.map = map;
        }

        public Function1<R, S> getMap() {
            return map;
        }
    }

}
