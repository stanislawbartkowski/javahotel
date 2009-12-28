/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.javahotel.common.toobject.AbstractTo;

public class VModelData implements IVModelData {

    private final AbstractTo a;

    public VModelData(AbstractTo a) {
        this.a = a;
    }

    public String getS(IVField fie) {
        VField f = (VField) fie;
        return a.getS(f.getFie());
    }

    public void setS(IVField fie, String s) {
        VField f = (VField) fie;
        a.setF(f.getFie(), s);
    }

    public AbstractTo getA() {
        return a;
    }

    public boolean isEmpty(IVField fie) {
        VField f = (VField) fie;
        return a.emptyS(f.getFie());
    }

}
