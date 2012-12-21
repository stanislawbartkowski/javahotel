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
package com.gwtmodel.table;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AVModelData by list of values
 * 
 * @author perseus
 */
public class VModelData extends AVModelData {

    private class V {

        private final IVField v;
        private Object val;

        V(IVField v) {
            this.v = v;
        }
    }

    /** List of IVFields and values. */
    private final List<V> ma = new ArrayList<V>();

    private V findV(IVField f) {
        for (V v : ma) {
            if (v.v.eq(f)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public Object getF(IVField fie) {
        V v = findV(fie);
        if (v == null) {
            return null;
        }
        return v.val;
    }

    @Override
    public void setF(IVField fie, Object o) {
        V v = findV(fie);
        if (v == null) {
            v = new V(fie);
            ma.add(v);
        }
        v.val = o;
    }

    @Override
    public boolean isValid(IVField fie) {
        // return findV(fie) != null;
        // on purpose: isValid for all IVField
        return true;
    }

    @Override
    public List<IVField> getF() {
        List<IVField> li = new ArrayList<IVField>();
        for (V v : ma) {
            li.add(v.v);
        }
        return li;
    }
}
