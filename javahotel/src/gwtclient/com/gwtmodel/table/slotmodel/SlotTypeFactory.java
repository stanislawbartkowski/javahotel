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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;

public class SlotTypeFactory {

    public SlotType construct(DataActionEnum dataActionEnum, IDataType dType) {
        return new SlotType(SlotEventEnum.DataAction, null, null,
                dataActionEnum, null, null, dType, null, null);
    }

    public SlotType construct(GetActionEnum gEnum, IDataType dType) {
        return new SlotType(SlotEventEnum.GetterCaller, null, null, null, null,
                null, dType, gEnum, null);
    }

    public SlotType construct(IDataType dType, IVField fie) {
        return new SlotType(SlotEventEnum.ChangeValue, fie, null, null, null,
                null, dType, null, null);
    }

    public SlotType construct(CellId cellId) {
        return new SlotType(SlotEventEnum.CallBackWidget, null, null, null,
                null, cellId, null, null, null);
    }

    public SlotType constructH(CellId cellId) {
        return new SlotType(SlotEventEnum.GetterCaller, null, null, null,
                null, cellId, null, GetActionEnum.GetHtmlForm, null);
    }

    public SlotType construct(int cellId) {
        return new SlotType(SlotEventEnum.CallBackWidget, null, null, null,
                null, new CellId(cellId), null, null, null);
    }

    public SlotType construct(ClickButtonType buttonClick) {
        return new SlotType(SlotEventEnum.ClickButton, null, buttonClick, null,
                null, null, null, null, null);
    }

    public SlotType construct(ClickButtonType.StandClickEnum clickEnum) {
        return new SlotType(SlotEventEnum.ClickButton, null,
                new ClickButtonType(clickEnum), null, null, null, null, null,
                null);
    }

    public SlotType construct(String stringButton) {
        return new SlotType(SlotEventEnum.ClickString, null, null, null, null,
                null, null, null, stringButton);
    }

    public SlotType construct() {
        return new SlotType(SlotEventEnum.ClickString, null, null, null, null,
                null, null, null, null);
    }
}
