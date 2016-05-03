/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

/**
 * @author hotel
 * 
 */
public class DataIntegerVDataSignal extends DataIntegerSignal {

    private final IVModelData v;
    private final boolean after;

    public DataIntegerVDataSignal(int i, IVModelData v, boolean after) {
        super(i);
        this.v = v;
        this.after = after;
    }

    IVModelData getV() {
        return v;
    }

    boolean isAfter() {
        return after;
    }

    private final static String SIGNAL_ID = DataIntegerVDataSignal.class
            .getName() + "PUBLIC_TABLE_ADD_ROWV";

    public static CustomStringSlot constructSlotAddRowSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }

}
