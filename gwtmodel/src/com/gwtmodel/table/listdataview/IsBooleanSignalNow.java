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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

/**
 * @author hotel
 * 
 */
public class IsBooleanSignalNow extends CustomObjectValue<Boolean> {

    private static final String GETISTREEVIEWNOW = "TABLE_PUBLIC_GET_TABLE_TREE_VIEW_SIGNAL_NOW";
    private static final String GETISTABLETREEENABLED = "TABLE_PUBLIC_GET_TABLE_TREE_ENABLED";
    private static final String GETISTABLEFILTER = "TABLE_PUBLIC_GET_TABLE_IS_FILTER";

    IsBooleanSignalNow(boolean value) {
        super(value);
    }

    public boolean isBoolInfo() {
        Boolean b = this.getValue();
        return b.booleanValue();
    }

    public static CustomStringSlot constructSlotGetTreeView(IDataType dType) {
        return new CustomStringDataTypeSlot(GETISTREEVIEWNOW, dType);
    }

    public static CustomStringSlot constructSlotGetTableTreeEnabled(
            IDataType dType) {
        return new CustomStringDataTypeSlot(GETISTABLETREEENABLED, dType);
    }
    
    public static CustomStringSlot constructSlotGetTableIsFilter(
            IDataType dType) {
        return new CustomStringDataTypeSlot(GETISTABLEFILTER, dType);
    }

}
