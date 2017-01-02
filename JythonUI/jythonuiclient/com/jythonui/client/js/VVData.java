/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.client.js;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.IVField;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

class VVData extends AVModelData {

    private final DialogVariables v;

    VVData(DialogVariables v) {
        this.v = v;
    }

    @Override
    public Object getF(IVField fie) {
        FieldValue val = v.getValue(fie.getId());
        return val.getValue();
    }

    @Override
    public void setF(IVField fie, Object o) {
    }

    @Override
    public boolean isValid(IVField fie) {
        return true;
    }

    @Override
    public List<IVField> getF() {
        List<IVField> l = new ArrayList<IVField>();
        for (String s : v.getFields()) {
            IVField iv = VField.construct(s, v.getValue(s));
            l.add(iv);
        }
        return l;
    }

}
