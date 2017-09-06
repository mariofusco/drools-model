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

package org.drools.model.impl;

import org.drools.model.Declaration;
import org.drools.model.Type;

public class DeclarationImpl<T> extends VariableImpl<T> implements Declaration<T> {
    private String entryPoint;

    public DeclarationImpl(Type<T> type) {
        super(type);
    }

    public DeclarationImpl(Type<T> type, String entryPoint) {
        super(type);
        this.entryPoint = entryPoint;
    }

    public DeclarationImpl(Type<T> type, String name, String entryPoint) {
        super(type, name);
        this.entryPoint = entryPoint;
    }

    @Override
    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint( String entryPoint ) {
        this.entryPoint = entryPoint;
    }
}
