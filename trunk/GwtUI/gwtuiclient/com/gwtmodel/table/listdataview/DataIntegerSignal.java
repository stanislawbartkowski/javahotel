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
public class DataIntegerSignal extends CustomObjectValue<Integer> {

    public DataIntegerSignal(int i) {
        super(i);
    }

    private final static String SIGNAL_ID_REMOVE = DataIntegerSignal.class
            .getName() + "PUBLIC_TABLE_REMOVE_ROW";
    private final static String SIGNAL_ID_GET = DataIntegerSignal.class
            .getName() + "PUBLIC_TABLE_GET_ROW";
    private final static String SIGNAL_SET_SIZE = DataIntegerSignal.class
            .getName() + "PUBLIC_TABLE_SET_SIZE";

    public static CustomStringSlot constructSlotRemoveVSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID_REMOVE);
    }

    public static CustomStringSlot constructSlotGetVSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID_GET);
    }

    public static CustomStringSlot constructSlotSetTableSize(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_SET_SIZE);
    }

}
