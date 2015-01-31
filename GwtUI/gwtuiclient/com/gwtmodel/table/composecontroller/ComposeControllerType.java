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
package com.gwtmodel.table.composecontroller;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotable;

public class ComposeControllerType {

    private final ISlotable iSlot;
    private final IDataType dType;
    private final int row, cell;
    private final CellId cellId;

    public ComposeControllerType(ISlotable iSlot, IDataType dType, int row,
            int cell) {
        this.iSlot = iSlot;
        this.dType = dType;
        this.row = row;
        this.cell = cell;
        this.cellId = null;
    }

    public ComposeControllerType(ISlotable iSlot) {
        this(iSlot, null, -1, -1);
    }

    public ComposeControllerType(ISlotable iSlot, IDataType dType) {
        this(iSlot, dType, -1, -1);
    }

    public ComposeControllerType(ISlotable iSlot, CellId cellId) {
        this.iSlot = iSlot;
        this.dType = null;
        this.row = -1;
        this.cell = -1;
        this.cellId = cellId;
    }

    public ComposeControllerType(ISlotable iSlot, IDataType dType, CellId cellId) {
        this.iSlot = iSlot;
        this.dType = dType;
        this.row = -1;
        this.cell = -1;
        this.cellId = cellId;
    }

    public boolean isPanelElem() {
        return row != -1;
    }

    public boolean isCellId() {
        return cellId != null;
    }

    public CellId getCellId() {
        return cellId;
    }

    public ISlotable getiSlot() {
        return iSlot;
    }

    public IDataType getdType() {
        return dType;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }

}
