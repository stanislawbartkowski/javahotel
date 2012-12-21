/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

    private static final String GETLINENOWRAP = IsBooleanSignalNow.class
            .getName() + "TABLE_PUBLIC_GETLINENOWRAP";
    private static final String SETLINENOWRAP = IsBooleanSignalNow.class
            .getName() + "TABLE_PUBLIC_SETLINENOWRAP";

    public IsBooleanSignalNow(boolean value) {
        super(value);
    }

    public boolean isBoolInfo() {
        Boolean b = this.getValue();
        return b.booleanValue();
    }

    public static CustomStringSlot constructSlotSetLineNoWrap(IDataType dType) {
        return new CustomStringDataTypeSlot(SETLINENOWRAP, dType);
    }

    public static CustomStringSlot constructSlotGetLineNoWrap(IDataType dType) {
        return new CustomStringDataTypeSlot(GETLINENOWRAP, dType);
    }
}
