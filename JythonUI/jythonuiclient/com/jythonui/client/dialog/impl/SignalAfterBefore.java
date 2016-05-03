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
package com.jythonui.client.dialog.impl;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

public class SignalAfterBefore implements ICustomObject {

    // default constructor
    SignalAfterBefore() {
    }

    private static final String AFTER_BEFORE_SIGNAL = SignalAfterBefore.class
            .getName() + "_AFTER_BEFORE_SIGNAL";

    public static CustomStringSlot constructSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, AFTER_BEFORE_SIGNAL);
    }

}
