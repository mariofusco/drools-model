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

import java.util.LinkedList;

import org.drools.model.Source;
import org.drools.model.Variable;
import org.drools.model.functions.Function1;
import org.drools.model.functions.Predicate1;
import org.drools.model.view.OOPathViewItem.OOPathChunk;
import org.drools.model.view.OOPathViewItem.OOPathHeadChunk;
import org.drools.model.view.OOPathViewItem.OOPathTailChunk;

public class OOPathBuilder<T> {

    private final Variable<T> variable;
    private final LinkedList<OOPathChunk<?>> chunks = new LinkedList<>();

    public OOPathBuilder( Variable<T> variable ) {
        this.variable = variable;
    }

    public <S> OOPathChunkBuilder<S> in( Source<S> source ) {
        chunks.add(new OOPathHeadChunk<S>( source ) );
        return new OOPathChunkBuilder<S>();
    }

    public class OOPathChunkEndBuilder<S> implements ViewItemBuilder<T> {
        public <T> OOPathChunkEndBuilder<T> map( Function1<S, T> map ) {
            chunks.add(new OOPathTailChunk<S, T>( map ) );
            return new OOPathChunkBuilder<T>();
        }

        @Override
        public ViewItem<T> get() {
            return new OOPathViewItem<>( variable, chunks );
        }
    }

    public class OOPathChunkBuilder<S> extends OOPathChunkEndBuilder<S> {
        public OOPathChunkEndBuilder<S> filter( Predicate1<S> filter ) {
            ((OOPathChunk<S>) chunks.getLast()).setFilter( filter );
            return this;
        }
    }
}
