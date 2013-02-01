/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.client.dialog;

import com.gwtmodel.table.IDataType;

/**
 * @author hotel
 * 
 */
class DataType implements IDataType {

    private final String id;
    private final DialogContainer d;

    private DataType(String id, DialogContainer d) {
        this.id = id;
        this.d = d;
    }

    static IDataType construct(String id, DialogContainer d) {
        return new DataType(id, d);
    }

    @Override
    public boolean eq(IDataType o) {
        DataType d = (DataType) o;
        return id.equals(d.id);
    }

    public @Override
    boolean equals(Object o) {
        IDataType d = (IDataType) o;
        return eq(d);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    DialogContainer getD() {
        return d;
    }

    String getId() {
        return id;
    }
    
}
