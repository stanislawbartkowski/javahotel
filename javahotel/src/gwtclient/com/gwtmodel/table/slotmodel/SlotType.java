/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.slotmodel;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;

public class SlotType {

    /** Slot event type. */
    private final SlotEventEnum slEnum;

    public SlotEventEnum getSlEnum() {
        return slEnum;
    }

    public IVField getFie() {
        return fie;
    }

    public ClickButtonType getButtonClick() {
        return buttonClick;
    }

    public ListEventEnum getListEvEnum() {
        return listEvEnum;
    }

    public List<SlotType> getSlList() {
        return slList;
    }

    /** Field type (ChangeValue) . */
    private final IVField fie;

    /** Button click number (ClickButton). */
    private final ClickButtonType buttonClick;

    /** List event (ListRefreshAftgerEvent). */
    private final ListEventEnum listEvEnum;

    /** Composite slot */
    private final List<SlotType> slList;

    /** Panel identifier for CallBackWidget. */
    private final int cellId;
    
    /** Data identifier for list. */
    private final IDataType dType;

    public boolean eq(SlotType slType) {
        if (slEnum != slType.slEnum) {
            return false;
        }
        switch (slEnum) {
        case CallBackWidget:
            return cellId == slType.cellId;
        case ListEvent:
            if (listEvEnum != slType.listEvEnum) { return false; }
            return dType.eq(slType.dType);
        case ClickButton:
            return buttonClick.eq(slType.buttonClick);
        }
        return true;
    }

    SlotType(SlotEventEnum slEnum, IVField fie, ClickButtonType buttonClick,
            ListEventEnum listEvEnum, List<SlotType> slList, int cellId, IDataType dType) {
        this.slEnum = slEnum;
        this.fie = fie;
        this.buttonClick = buttonClick;
        this.listEvEnum = listEvEnum;
        this.slList = slList;
        this.cellId = cellId;
        this.dType = dType;
    }

    public int getCellId() {
        return cellId;
    }

    public IDataType getdType() {
        return dType;
    }
}
