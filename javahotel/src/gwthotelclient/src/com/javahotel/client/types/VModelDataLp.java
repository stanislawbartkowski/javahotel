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
package com.javahotel.client.types;

import java.util.List;

import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.IVField;
import com.javahotel.common.toobject.AbstractTo;

/**
 * @author hotel
 * 
 */
class VModelDataLp extends AbstractLpVModelData implements HModelData {

    private final HModelData v;

    VModelDataLp(AbstractTo a) {
        v = VModelDataFactory.construct(a);
    }

    @Override
    public List<IVField> getF() {
        List<IVField> li = v.getF();
        addV(li); // add Lp
        return li;
    }

    @Override
    public Object getF(IVField fie) {
        if (super.isValid(fie)) {
            return super.getF(fie);
        }
        return v.getF(fie);
    }

    @Override
    public void setF(IVField fie, Object val) {
        if (super.isValid(fie)) {
            super.setF(fie, val);
            return;
        }
        v.setF(fie, val);
    }

    @Override
    public boolean isValid(IVField fie) {
        if (super.isValid(fie)) {
            return true;
        }
        return v.isValid(fie);
    }

    @Override
    public AbstractTo getA() {
        return v.getA();
    }

}
