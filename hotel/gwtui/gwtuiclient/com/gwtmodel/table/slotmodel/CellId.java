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
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.common.CUtil;

public class CellId {

    private final int mainId;
    private final int subId;
    private final String mainS;
    private IDataType dType;

    public CellId(int mainId, int subId) {
        this.mainId = mainId;
        this.subId = subId;
        mainS = null;
        dType = null;
    }

    public CellId(int mainId) {
        this.mainId = mainId;
        this.subId = 0;
        mainS = null;
        dType = null;
    }

    public CellId(String mainS, int subId) {
        this.mainId = -1;
        this.subId = subId;
        this.mainS = mainS;
        dType = null;
    }

    public CellId(String mainS) {
        this.mainId = -1;
        this.subId = 0;
        this.mainS = mainS;
        dType = null;
    }

    boolean eq(CellId s) {
        if (subId != s.subId) {
            return false;
        }
        if (mainS != null) {
            return CUtil.EqNS(mainS, s.mainS);
        }
        return (mainId == s.mainId);
    }

    public CellId constructNext(IDataType publishType) {
        int nId = subId + 1;
        CellId i;
        i = new CellId(mainId, nId);
        i.dType = publishType;
        return i;
    }

    /**
     * @return the dType
     */
    public IDataType getdType() {
        return dType;
    }
}
