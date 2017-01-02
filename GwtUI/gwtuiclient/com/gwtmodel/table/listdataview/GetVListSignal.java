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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

import java.util.List;

/**
 * @author hotel
 * 
 */
public class GetVListSignal extends DataIntegerSignal {

    private final List<IGetSetVField> vList;

    GetVListSignal(List<IGetSetVField> vList) {
        super(-1);
        this.vList = vList;
    }

    public GetVListSignal(int i) {
        super(i);
        this.vList = null;
    }

    /**
     * @return the vList
     */
    public List<IGetSetVField> getvList() {
        return vList;
    }

    private static final String GETVSIGNAL = GetVListSignal.class.getName()
            + "TABLE_PUBLIC_LISTDATAVIEW-GETVSIGNAL";

    public static CustomStringSlot constructSlotGetVSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, GETVSIGNAL);
    }

}
