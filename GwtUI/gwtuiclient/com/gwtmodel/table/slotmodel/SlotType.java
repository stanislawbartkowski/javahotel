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
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IEquatable;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.LogT;
import java.util.List;

public class SlotType implements IEquatable<SlotType> {

    /**
     * Slot event type.
     */
    private final SlotEventEnum slEnum;
    private final DataActionEnum dataActionEnum;
    /**
     * Field type (ChangeValue) .
     */
    private final IVField fie;
    /**
     * Button click number (ClickButton).
     */
    private final ButtonAction bAction;
    private final ClickButtonType buttonClick;
    /**
     * Composite slot
     */
    private final List<SlotType> slList;
    /**
     * Panel identifier for CallBackWidget.
     */
    private final CellId cellId;
    /**
     * Data identifier for list.
     */
    private final IDataType dType;
    /**
     * Getter.
     */
    private final GetActionEnum gEnum;
    /**
     * Custom
     */
    private final ISlotCustom iEq;

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

    public List<SlotType> getSlList() {
        return slList;
    }

    @Override
    public boolean eq(SlotType slType) {
        if (slEnum != slType.slEnum) {
            return false;
        }
        switch (slEnum) {
        case Custom:
            return slType.getiEq().eq(getiEq());
        case ChangeValue:
            if (fie != null && slType.getFie() != null) {
                if (!fie.eq(slType.getFie())) {
                    return false;
                }
            }
            return dType.eq(slType.dType);
        case CallBackWidget:
            if (!dType.eq(slType.dType)) {
                return false;
            }
            return cellId.eq(slType.cellId);
        case ButtonAction:
            if (!bAction.getAction().equals(slType.bAction.getAction())) {
                return false;
            }
            if (!Utils.eqI(dType, slType.dType)) {
                return false;
            }
            return buttonClick.eq(slType.buttonClick);
        case DataAction:
            if (dataActionEnum != slType.dataActionEnum) {
                return false;
            }
            return Utils.eqI(dType, slType.dType);
        case GetterCaller:
            if (gEnum != slType.gEnum) {
                return false;
            }
            if (gEnum == GetActionEnum.GetHtmlMainForm) {
                return true;
            }
            if (gEnum == GetActionEnum.GetHtmlForm) {
                return cellId.eq(slType.cellId);
            }
            return dType.eq(slType.dType);
        }
        return true;
    }

    public GetActionEnum getgEnum() {
        return gEnum;
    }

    SlotType(SlotEventEnum slEnum, IVField fie, ClickButtonType buttonClick,
            DataActionEnum dataActionEnum, List<SlotType> slList,
            CellId cellId, IDataType dType, GetActionEnum gEnum,
            ISlotCustom iEq, ButtonAction bAction) {
        this.slEnum = slEnum;
        this.fie = fie;
        this.buttonClick = buttonClick;
        this.dataActionEnum = dataActionEnum;
        this.slList = slList;
        this.cellId = cellId;
        this.dType = dType;
        this.gEnum = gEnum;
        this.iEq = iEq;
        this.bAction = bAction;
    }

    public CellId getCellId() {
        return cellId;
    }

    public IDataType getdType() {
        return dType;
    }

    @Override
    public String toString() {
        Object o1 = null;
        Object o2 = null;
        switch (slEnum) {
        case ChangeValue:
            o1 = fie;
            break;
        case ButtonAction:
            o1 = getbAction();
            break;
        case CallBackWidget:
            o1 = cellId;
            break;
        case DataAction:
            o1 = dataActionEnum;
            o2 = dType;
            break;
        case GetterCaller:
            o1 = gEnum;
            o2 = dType;
            break;
        case Custom:
            o1 = getiEq();
            break;
        }
        if (o1 == null) {
            return LogT.getT().slStringLog(slEnum.toString());
        }
        if (o2 == null) {
            return LogT.getT().slStringLog1(slEnum.toString(), o1.toString());
        }
        return LogT.getT().slStringLog2(slEnum.toString(), o1.toString(),
                o2.toString());
    }

    /**
     * @return the bAction
     */
    public ButtonAction getbAction() {
        return bAction;
    }

    /**
     * @return the iEq
     */
    public ISlotCustom getiEq() {
        return iEq;
    }
}
