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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.ISlotCustom;

/**
 * @author hotel
 * 
 */
public class ClickColumnImageSignal extends DataIntegerSignal {

    private final WSize w;
    private final IVField v;
    private final int imno;

    ClickColumnImageSignal(WSize w, int row, IVField v, int imno) {
        super(row);
        this.w = w;
        this.v = v;
        this.imno = imno;
    }

    /**
     * @return the w
     */
    public WSize getW() {
        return w;
    }

    /**
     * @return the v
     */
    public IVField getV() {
        return v;
    }

    /**
     * @return the imno
     */
    public int getImno() {
        return imno;
    }

    private static final String SIGNAL_ID = ClickColumnImageSignal.class
            .getName() + "TABLE_PUBLIC_CLICK_IMAGE_COL";

    public static ISlotCustom constructSlotClickColumnSignal(
            IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }

}
