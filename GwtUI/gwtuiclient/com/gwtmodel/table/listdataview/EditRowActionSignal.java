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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

/**
 * @author hotel
 * 
 */
public class EditRowActionSignal implements ICustomObject {

    private final int rownum;
    private final PersistTypeEnum e;
    private final WSize w;

    EditRowActionSignal(int rownum, PersistTypeEnum e, WSize w) {
        assert e != PersistTypeEnum.SHOWONLY : LogT.getT()
                .ValueNotExpectedHere();
        this.rownum = rownum;
        this.e = e;
        this.w = w;
    }

    /**
     * @return the rownum
     */
    public int getRownum() {
        return rownum;
    }

    /**
     * @return the e
     */
    public PersistTypeEnum getE() {
        return e;
    }

    /**
     * @return the w
     */
    public WSize getW() {
        return w;
    }

    private static final String EDIT_ROW_ACTION_SIGNAL = EditRowActionSignal.class
            .getName() + "TABLE_PUBLIC_EDIT_ROW_ACTION_SIGNAL";

    public static CustomStringSlot constructSlotEditActionSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, EDIT_ROW_ACTION_SIGNAL);
    }

}
