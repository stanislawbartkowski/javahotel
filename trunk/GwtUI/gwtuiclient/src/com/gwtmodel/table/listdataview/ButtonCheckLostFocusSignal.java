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
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

/**
 * @author hotel
 * 
 */
public class ButtonCheckLostFocusSignal extends
        CustomObjectValue<ClickButtonType> {

    private final IDataType buttondType;

    /**
     * @param value
     */
    public ButtonCheckLostFocusSignal(ClickButtonType value) {
        super(value);
        this.buttondType = null;
    }

    public ButtonCheckLostFocusSignal(ClickButtonType value,
            IDataType buttondType) {
        super(value);
        this.buttondType = buttondType;
    }

    private static final String SIGNAL_ID = ButtonCheckLostFocusSignal.class
            .getName() + "_TABLE_PUBLIC_BUTTON_CHECK_FOCUS_SIGNAL";
    private static final String SIGNAL_ID_BACK = ButtonCheckLostFocusSignal.class
            .getName() + "TABLE_PUBLIC_BUTTON_CHECK_FOCUS_BACK_SIGNAL";

    public static CustomStringSlot constructSlotButtonCheckFocusSignal(
            IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }

    static SlotType constructSlotButtonCheckBackFocusSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(dType,
                SIGNAL_ID_BACK);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(dType, slo);
    }

    /**
     * @return the buttondType
     */
    IDataType getButtondType() {
        return buttondType;
    }
}
