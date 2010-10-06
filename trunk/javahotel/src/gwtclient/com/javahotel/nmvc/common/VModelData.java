/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.common;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.IVField;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

public class VModelData extends AVModelData {

    private final AbstractTo a;

    public VModelData(AbstractTo a) {
        this.a = a;
    }

    private IField toF(IVField fie) {
        VField f = (VField) fie;
        IField fi = f.getFie();
        return fi;
    }

    public String getS(IVField fie) {
        return a.getS(toF(fie));
    }

    public Object getF(IVField fie) {
        return a.getF(toF(fie));
    }
    
    public void setF(IVField fie, Object val) {
        a.setF(toF(fie), val);
    }

    public AbstractTo getA() {
        return a;
    }

    public boolean isEmpty(IVField fie) {
        return a.emptyS(toF(fie));
    }

    public IVField[] getF() {
        IVField[] e = new IVField[a.getT().length];
        for (int i = 0; i < e.length; i++) {
            e[i] = new VField(a.getT()[i]);
        }
        return e;
    }

}
