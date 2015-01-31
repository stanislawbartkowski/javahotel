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
package com.gwtmodel.table.controlbuttonview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;

/**
 *
 * @author perseus
 */
public class ButtonSendListOfButtons extends
        CustomObjectValue<IContrButtonView> {

    ButtonSendListOfButtons(IContrButtonView cList) {
        super(cList);
    }

    private static final String SIGNAL_ID = ButtonSendListOfButtons.class
            .getName() + "BUTTON_PUBLIC_SEND_LIST_OF_BUTTONS";

    public static CustomStringSlot constructSlotSendListOfButtons(
            IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }
}
