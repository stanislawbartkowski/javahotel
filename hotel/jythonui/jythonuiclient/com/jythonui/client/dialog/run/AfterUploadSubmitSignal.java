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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

public class AfterUploadSubmitSignal extends CustomObjectValue<String> {

    private final WSize wS;
    private final String submitId;

    AfterUploadSubmitSignal(String result, WSize wS, String submitId) {
        super(result);
        this.wS = wS;
        this.submitId = submitId;
    }

    public WSize getwS() {
        return wS;
    }

    public String getSubmitId() {
        return submitId;
    }

    private static final String SUBMIT_SIGNAL = AfterUploadSubmitSignal.class
            .getName() + "_AFTER_SUBMIT_SIGNAL";

    public static CustomStringSlot constructSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SUBMIT_SIGNAL);
    }

}
