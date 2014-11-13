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
package com.gwtmodel.table.controlbuttonview;

import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotType;

/**
 * @author hotel
 *
 */
public class ButtonRedirectSignal implements ICustomObject {

    private final SlotType sl;
    private final IDataType buttType;
    private final ClickButtonType bType;
    private GWidget w;

    public ButtonRedirectSignal(SlotType sl, IDataType buttType,
            ClickButtonType bType) {
        this.sl = sl;
        this.bType = bType;
        this.buttType = buttType;
    }

    /**
     * @return the sl
     */
    SlotType getSl() {
        return sl;
    }

    /**
     * @return the bType
     */
    ClickButtonType getbType() {
        return bType;
    }

    /**
     * @param w
     *            the w to set
     */
    void setW(GWidget w) {
        this.w = w;
    }

    public void sendButtonSignal(ISlotable i) {
        i.getSlContainer().publish(buttType, bType, w);
    }

    private static final String SIGNAL_ID = ButtonRedirectSignal.class
            .getName() + "BUTTON_PUBLIC_REDIRECT_SIGNAL";

    public static CustomStringSlot constructSlotButtonRedirectSignal(
            IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }
}
