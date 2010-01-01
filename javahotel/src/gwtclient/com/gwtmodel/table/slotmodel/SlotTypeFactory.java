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

public class SlotTypeFactory {

    public SlotType construct(DataActionEnum dataActionEnum, IDataType dType) {
        return new SlotType(SlotEventEnum.DataAction, null, null,
                dataActionEnum, null, -1, dType, null);
    }

    public SlotType construct(GetActionEnum gEnum, IDataType dType) {
        return new SlotType(SlotEventEnum.GetterCaller, null, null, null, null,
                -1, dType, gEnum);
    }

    public SlotType construct(int cellId) {
        return new SlotType(SlotEventEnum.CallBackWidget, null, null, null,
                null, cellId, null, null);
    }

    public SlotType construct(ClickButtonType buttonClick) {
        return new SlotType(SlotEventEnum.ClickButton, null, buttonClick, null,
                null, -1, null, null);
    }

    public SlotType construct(ClickButtonType.StandClickEnum clickEnum) {
        return new SlotType(SlotEventEnum.ClickButton, null,
                new ClickButtonType(clickEnum), null, null, -1, null, null);
    }

}
