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
package com.gwtmodel.table.slotmodel;

public class CellId {

    private final int mainId;
    private final int subId;

    public CellId(int mainId, int subId) {
        this.mainId = mainId;
        this.subId = subId;
    }
    
    public CellId(int mainId) {
        this.mainId = mainId;
        this.subId = 0;
    }

    boolean eq(CellId s) {
        return (mainId == s.mainId) && (subId == s.subId);
    }
    
    public CellId constructNext() {
        int nId = subId + 1;
        return new CellId(mainId,nId);
    }

}
