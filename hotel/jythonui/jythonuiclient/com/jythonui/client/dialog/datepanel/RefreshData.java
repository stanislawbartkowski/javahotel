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
package com.jythonui.client.dialog.datepanel;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.jythonui.shared.DateLineVariables;

public class RefreshData extends CustomObjectValue<DateLineVariables> {

    public RefreshData(DateLineVariables v) {
        super(v);
    }

    private static final String REFRESH_SIGNAL = RefreshData.class.getName()
            + "_REFRESH_DATA";

    private static final String REQUEST_REFRESH_SIGNAL = RefreshData.class
            .getName() + "REQUEST_REFRESH_DATA";

    public static CustomStringSlot constructRefreshData(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, REFRESH_SIGNAL);
    }

    public static CustomStringSlot constructRequestForRefreshData(
            IDataType dType) {
        return new CustomStringDataTypeSlot(dType, REQUEST_REFRESH_SIGNAL);
    }

}
