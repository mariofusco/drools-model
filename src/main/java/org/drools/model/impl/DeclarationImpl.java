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
import org.drools.model.Window;

public class DeclarationImpl<T> extends VariableImpl<T> implements Declaration<T> {
    private String entryPoint;
    private Window window;

    public DeclarationImpl(Type<T> type) {
        super(type);
    }

    public DeclarationImpl(Type<T> type, String name) {
        super(type, name);
    }

    @Override
    public String getEntryPoint() {
        return entryPoint;
    }

    public DeclarationImpl<T> setEntryPoint( String entryPoint ) {
        this.entryPoint = entryPoint;
        return this;
    }

    @Override
    public Window getWindow() {
        return window;
    }

    public DeclarationImpl<T> setWindow( Window window ) {
        this.window = window;
        return this;
    }
}
