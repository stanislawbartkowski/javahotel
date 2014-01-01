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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

/**
 * @author hotel
 * 
 */
public class SetSortColumnSignal extends CustomObjectValue<IVField> {

    private final boolean inc;
    
    public SetSortColumnSignal(IVField value, boolean inc) {
        super(value);
        this.inc = inc;
    }
    
    boolean isInc() {
        return inc;
    }

    private static final String SIGNAL_ID = SetSortColumnSignal.class.getName()
            + "TABLE_PUBLIC_SET_SORT_COLUMN";

    public static CustomStringSlot constructSlotSetSortColumnSignal(
            IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }

}
