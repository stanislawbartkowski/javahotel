/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.client.dialog.run;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

public class CloseDialogByImage implements ICustomObject {

    // default constructor
    CloseDialogByImage() {
    }

    private static final String CLOSE_SIGNAL = CloseDialogByImage.class
            .getName() + "_CLOSE_DIALOG_BY_IMAGE";

    public static CustomStringSlot constructSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, CLOSE_SIGNAL);
    }

}
