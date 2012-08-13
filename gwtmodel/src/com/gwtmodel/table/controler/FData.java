/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.*;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for keeping data for 'Find' and 'Filter' functionality
 * 
 * @author perseus
 */
class FData extends VModelData {

    /** List of all fields with find/filter values. */
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

    /**
     * Check if any data have been entered
     * 
     * @return true: nothing, is empty false: not empty
     */
    public boolean isEmpty() {
        for (IVField f : getF()) {
            if (f.getType().getType() == FieldDataType.T.BOOLEAN) {
                FField fie = (FField) f;
                if (fie.isIgnoreField()) {
                    continue;
                }
                if (fie.isCheckField()) {
                    continue;
                }
                FField ignore = FField.constructIgnore(fie.getV());
                Boolean b = (Boolean) this.getF(ignore);
                if ((b != null) && b.booleanValue()) {
                    continue;
                }
                return false;
            }
            if (!FUtils.isNullValue(this, f)) {
                return false;
            }
        }
        return true;
    }

    private boolean inRange(IVModelData row) {
        for (VListHeaderDesc v : li) {            
            FField ignore = FField.constructIgnore(v);
            Boolean b = (Boolean) this.getF(ignore);
            if (b != null) {
                // boolean field because ignore exists
                if (b.booleanValue()) {
                    // ignore, do not participate in comparing
                    continue;   
                }
            }
            FField from = FField.constructFrom(v);
            FField to = FField.constructTo(v);
            IVField check = FField.constructCheck(v);
            if (!FUtils.inRange(row, v.getFie(), this, from, to, check)) {
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
