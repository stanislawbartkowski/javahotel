/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.dialog.jsearch;

import java.util.List;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.FieldValue;

class JSearchList implements IOkModelData {

    private final FieldValue val;
    private final IVField fId;

    JSearchList(String fT, FieldValue val) {
        this.fId = VField.construct(fT);
        this.val = val;
    }

    @Override
    public boolean OkData(IVModelData row) {
        Object oval = row.getF(fId);
        if (val == null)
            return false;
        int res = FUtils.compareValue(val.getValue(), oval, val.getType(), 0);
        return res == 0;
    }

    @Override
    public List<ValidationData> getValList() {
        return null;
    }

}
