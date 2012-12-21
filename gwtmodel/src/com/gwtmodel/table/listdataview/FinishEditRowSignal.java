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
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.controlbuttonview.ButtonRedirectSignal;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

/**
 * @author hotel
 * 
 */
public class FinishEditRowSignal extends CustomObjectValue<WChoosedLine> {

    private boolean okChangeLine = true;
    private final WChoosedLine nextW;
    private final ButtonRedirectSignal bRedir;

    FinishEditRowSignal(WChoosedLine lostFocus, WChoosedLine nextW) {
        super(lostFocus);
        this.nextW = nextW;
        this.bRedir = null;
    }
    
    FinishEditRowSignal(WChoosedLine lostFocus, ButtonRedirectSignal bRedir) {
        super(lostFocus);
        this.nextW = null;
        this.bRedir = bRedir;
    }


    /**
     * @return the okChangeLine
     */
    boolean isOkChangeLine() {
        return okChangeLine;
    }

    public void setDoNotChange() {
        okChangeLine = false;
    }

    WChoosedLine getNextW() {
        return nextW;
    }
    
    /**
     * @return the bRedir
     */
    ButtonRedirectSignal getbRedir() {
        return bRedir;
    }

    private static final String SIGNAL_ID = "TABLE_PUBLIC_FINISH_EDIT_ROW_SIGNAL";
    private static final String SIGNAL_RETURN_ID = "TABLE_PUBLIC_FINISH_EDIT_ROW_RETURN_SIGNAL";

    public static CustomStringSlot constructSlotFinishEditRowSignal(
            IDataType dType) {
        return new CustomStringDataTypeSlot(SIGNAL_ID, dType);
    }

    public static CustomStringSlot constructSlotFinishEditRowReturnSignal(
            IDataType dType) {
        return new CustomStringDataTypeSlot(SIGNAL_RETURN_ID, dType);
    }

}
