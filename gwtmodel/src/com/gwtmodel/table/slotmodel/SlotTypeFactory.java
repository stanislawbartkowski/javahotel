/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.LogT;

public class SlotTypeFactory {

    public SlotType construct(DataActionEnum dataActionEnum, IDataType dType) {
        return new SlotType(SlotEventEnum.DataAction, null, null,
                dataActionEnum, null, null, dType, null, null, null);
    }

    public SlotType construct(GetActionEnum gEnum, IDataType dType) {
        return new SlotType(SlotEventEnum.GetterCaller, null, null, null, null,
                null, dType, gEnum, null, null);
    }

    public SlotType construct(IDataType dType, IVField fie) {
        return new SlotType(SlotEventEnum.ChangeValue, fie, null, null, null,
                null, dType, null, null, null);
    }

    public SlotType constructI(IDataType dType) {
        return new SlotType(SlotEventEnum.GetterCaller, null, null, null, null,
                null, dType, GetActionEnum.GetFormFieldWidget, null, null);
    }

    public SlotType construct(IDataType dType, CellId cellId) {
        assert dType != null : LogT.getT().dTypeCannotBeNull();
        assert cellId != null : LogT.getT().cannotBeNull();
        return new SlotType(SlotEventEnum.CallBackWidget, null, null, null,
                null, cellId, dType, null, null, null);
    }

    public SlotType constructH(CellId cellId) {
        assert cellId != null : LogT.getT().cannotBeNull();
        return new SlotType(SlotEventEnum.GetterCaller, null, null, null, null,
                cellId, null, GetActionEnum.GetHtmlForm, null, null);
    }

    public SlotType construct(IDataType dType, int cellId) {
        assert dType != null : LogT.getT().dTypeCannotBeNull();
        return new SlotType(SlotEventEnum.CallBackWidget, null, null, null,
                null, new CellId(cellId), dType, null, null, null);
    }

    public SlotType construct(ClickButtonType buttonClick, ButtonAction bAction) {
        return new SlotType(SlotEventEnum.ButtonAction, null, buttonClick,
                null, null, null, null, null, null, bAction);
    }

    public SlotType construct(IDataType dType, ClickButtonType buttonClick,
            ButtonAction bAction) {
        return new SlotType(SlotEventEnum.ButtonAction, null, buttonClick,
                null, null, null, dType, null, null, bAction);
    }

    public SlotType construct(ClickButtonType buttonClick) {
        return new SlotType(SlotEventEnum.ButtonAction, null, buttonClick,
                null, null, null, null, null, null, ButtonAction.ClickButton);
    }

    public SlotType construct(String stringButton) {
        return construct(new CustomStringSlot(stringButton));
    }

    public SlotType construct(ISlotCustom iEq) {
        return new SlotType(SlotEventEnum.Custom, null, null, null, null, null,
                null, null, iEq, null);
    }

    public SlotType construct(IDataType dType, SlotType sl) {
        return new SlotType(sl.getSlEnum(), sl.getFie(), sl.getButtonClick(),
                sl.getDataActionEnum(), sl.getSlList(), sl.getCellId(), dType,
                sl.getgEnum(), sl.getiEq(), sl.getbAction());
    }
}
