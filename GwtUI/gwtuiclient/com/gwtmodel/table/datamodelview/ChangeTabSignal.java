/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.datamodelview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.ISlotCustom;

/**
 *
 * @author perseus
 */
public class ChangeTabSignal extends CustomObjectValue<String> {

    public ChangeTabSignal(String tabId) {
        super(tabId);
    }

    private static final String SIGNAL_CHANGE_TAB = SignalChangeMode.class
            .getName() + "SET_TAB_PANEL";

    public static ISlotCustom constructSlot(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_CHANGE_TAB);
    }
}
