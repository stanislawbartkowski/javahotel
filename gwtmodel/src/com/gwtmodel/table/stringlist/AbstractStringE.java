/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.stringlist;

import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.IVField;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStringE extends AbstractLpVModelData {

    private String s;
    private Object o;

    protected List<IVField> createF(IVField fie) {
        List<IVField> li = new ArrayList<IVField>();
        li.add(fie);
        return addV(li);
    }

    @Override
    public Object getF(IVField fie) {
        if (super.isValid(fie)) {
            return super.getF(fie);
        }
        return s;
    }

    @Override
    public void setF(IVField fie, Object val) {
        if (super.isValid(fie)) {
            super.setF(fie, val);
            return;
        }
        this.s = (String) val;
    }

    @Override
    public Object getCustomData() {
        return o;
    }

    @Override
    public void setCustomData(Object o) {
        this.o = o;
    }

}
