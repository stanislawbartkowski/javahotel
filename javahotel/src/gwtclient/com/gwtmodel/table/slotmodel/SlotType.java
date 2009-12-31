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
    
    private final DataActionEnum dataActionEnum;

    public DataActionEnum getDataActionEnum() {
        return dataActionEnum;
    }

    public SlotEventEnum getSlEnum() {
        return slEnum;
    }

    public IVField getFie() {
        return fie;
    }

    public ClickButtonType getButtonClick() {
        return buttonClick;
    }

//    public PersistEventEnum getListEvEnum() {
//        return listEvEnum;
//    }

    public List<SlotType> getSlList() {
        return slList;
    }

    /** Field type (ChangeValue) . */
    private final IVField fie;

    /** Button click number (ClickButton). */
    private final ClickButtonType buttonClick;

    /** List event (ListRefreshAftgerEvent). */
  //  private final PersistEventEnum listEvEnum;

    /** Composite slot */
    private final List<SlotType> slList;

    /** Panel identifier for CallBackWidget. */
    private final int cellId;

    /** Data identifier for list. */
    private final IDataType dType;

    /** Validate action. */
//    private final ValidateActionType vEnum;

    /** Getter. */
    private final GetActionEnum gEnum;

    public boolean eq(SlotType slType) {
        if (slEnum != slType.slEnum) {
            return false;
        }
        switch (slEnum) {
        case CallBackWidget:
            return cellId == slType.cellId;
//        case PersistSignal:
//            if (listEvEnum != slType.listEvEnum) {
//                return false;
//            }
//            return dType.eq(slType.dType);
        case ClickButton:
            return buttonClick.eq(slType.buttonClick);
        case DataAction:
            if (dataActionEnum != slType.dataActionEnum) { return false; }
            if (dType == null) {
                return slType.dType == null;
            }
            if (slType.dType == null) { return false; }
            return dType.eq(slType.dType);
//        case ValidateAction:
//            if (!vEnum.eq(slType.vEnum)) {
//                return false;
//            }
//            return dType.eq(slType.dType);
        case GetterCaller:
            if (gEnum != slType.gEnum) {
                return false;
            }
            return dType.eq(slType.dType);
        }
        return true;
    }

//    public ValidateActionType getvEnum() {
//        return vEnum;
//    }

    public GetActionEnum getgEnum() {
        return gEnum;
    }

    SlotType(SlotEventEnum slEnum, IVField fie, ClickButtonType buttonClick,
            DataActionEnum dataActionEnum, List<SlotType> slList, int cellId,
            IDataType dType, GetActionEnum gEnum) {
        this.slEnum = slEnum;
        this.fie = fie;
        this.buttonClick = buttonClick;
        this.dataActionEnum = dataActionEnum;
        this.slList = slList;
        this.cellId = cellId;
        this.dType = dType;
        this.gEnum = gEnum;
    }

    public int getCellId() {
        return cellId;
    }

    public IDataType getdType() {
        return dType;
    }
}
