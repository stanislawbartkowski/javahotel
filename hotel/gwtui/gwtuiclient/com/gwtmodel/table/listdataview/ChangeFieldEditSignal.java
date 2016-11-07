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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import com.gwtmodel.table.slotmodel.ISlotable;

/**
 * 
 * @author perseus
 */
public class ChangeFieldEditSignal extends DataIntegerSignal {

    private final IVField v;
    private final WSize w;
    private final boolean before;
    private final IDataType dType;

    ChangeFieldEditSignal(IDataType dType, boolean before, int rownum,
            IVField v, WSize w) {
        super(rownum);
        this.v = v;
        this.w = w;
        this.before = before;
        this.dType = dType;
    }

    private final static String SIGNAL_CHANGE_FIELD = ChangeFieldEditSignal.class
            .getName() + "PUBLIC_TABLE_CHANGE_FIELD";

    public static CustomStringSlot constructSlotChangeEditSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_CHANGE_FIELD);
    }

    /**
     * @return the v
     */
    public IVField getV() {
        return v;
    }

    /**
     * @return the w
     */
    public WSize getW() {
        return w;
    }

    /**
     * @return the before
     */
    public boolean isBefore() {
        return before;
    }

    private static final String RET_SIGNAL_ID = ChangeFieldEditSignal.class
            .getName() + "TABLE_PUBLIC_RETURN_CHANGE_FOCUS_SIGNAL";

    static ISlotCustom constructReturnChangeSlotSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, RET_SIGNAL_ID);
    }

    public void signalFinishChangeSignal(ISlotable s) {
        ISlotCustom sl = constructReturnChangeSlotSignal(dType);
        s.getSlContainer().publish(sl, this);
    }

}
