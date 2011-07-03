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
package com.gwtmodel.table.controler;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class FData extends VModelData {

    private final List<FormField> liF;
    private final List<VListHeaderDesc> li;
    private final IOkModelData iOk;

    FData(List<FormField> liF, List<VListHeaderDesc> li) {
        this.liF = liF;
        this.li = li;
        iOk = new IOk();
    }

    @Override
    public List<IVField> getF() {
        List<IVField> ll = new ArrayList<IVField>();
        for (FormField v : liF) {
            ll.add(v.getFie());
        }
        return ll;
    }

    public boolean isEmpty() {
        for (IVField f : getF()) {
            if (f.getType().getType() == FieldDataType.T.BOOLEAN) {
                continue;
            }
            if (!FUtils.isNullValue(this, f)) {
                return false;
            }
        }
        return true;
    }

    private boolean inRange(IVModelData row) {
        for (VListHeaderDesc v : li) {
            FField from = new FField(v.getFie(), true, v);
            FField to = new FField(v.getFie(), false, v);
            if (!FUtils.inRange(row, v.getFie(), this, from, to)) {
                return false;
            }
        }
        return true;
    }

    private class IOk implements IOkModelData {

        public boolean OkData(IVModelData row) {
            return inRange(row);
        }
    }

    IOkModelData constructIOk() {
        return iOk;
    }
}
