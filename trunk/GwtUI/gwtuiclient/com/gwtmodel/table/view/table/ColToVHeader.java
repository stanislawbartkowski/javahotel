/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.table;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderDesc;

class ColToVHeader {

    private final Map<Integer, VListHeaderDesc> iMap = new HashMap<Integer, VListHeaderDesc>();
    private boolean actionCol = false;

    void addColDesc(int colpos, VListHeaderDesc v) {
        iMap.put(colpos, v);
    }

    void setActionCol(boolean set) {
        this.actionCol = set;
    }

    VListHeaderDesc getV(int colpos) {
        if (actionCol) {
            // action column
            if (colpos == 0)
                return null;
            colpos--;
        }
        VListHeaderDesc he = iMap.get(colpos);
        if (he == null) {
            String mess = LogT.getT().NullValueHeaderPos(colpos);
            Utils.internalErrorAlert(mess);
        }
        return he;
    }

}
