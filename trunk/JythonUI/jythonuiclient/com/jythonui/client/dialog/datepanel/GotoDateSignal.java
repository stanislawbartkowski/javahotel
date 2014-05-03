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
package com.jythonui.client.dialog.datepanel;

import java.util.Date;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

public class GotoDateSignal extends CustomObjectValue<Date> {

    private final static String GOTO_SIGNAL = GotoDateSignal.class.getName()
            + "_GOTO_SIGNAL";

    public GotoDateSignal(Date d) {
        super(d);
    }

    public static CustomStringSlot constructSlot(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, GOTO_SIGNAL);
    }

}
