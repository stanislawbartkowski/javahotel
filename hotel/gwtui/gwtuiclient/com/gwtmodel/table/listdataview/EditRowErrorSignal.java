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
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

/**
 * @author hotel
 * 
 */
public class EditRowErrorSignal extends DataIntegerSignal {

    private final InvalidateFormContainer errLine;

    public EditRowErrorSignal(int rownum, InvalidateFormContainer errLine) {
        super(rownum);
        this.errLine = errLine;
    }

    /**
     * @return the errLine
     */
    public InvalidateFormContainer getErrLine() {
        return errLine;
    }

    private static final String SIGNAL_ID = EditRowErrorSignal.class.getName()
            + "TABLE_PUBLIC_SET_LINE_ERROR";

    public static CustomStringSlot constructSlotLineErrorSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }

}
