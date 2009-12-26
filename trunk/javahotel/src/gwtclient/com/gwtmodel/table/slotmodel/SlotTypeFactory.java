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

public class SlotTypeFactory {

    private SlotTypeFactory() {

    }

    public static SlotType contruct(ListEventEnum listEvEnum) {
        return new SlotType(SlotEventEnum.ListEvent, null, null, listEvEnum,
                null, -1);
    }

    public static SlotType constructCallBackWidget(int cellId) {
        return new SlotType(SlotEventEnum.CallBackWidget, null, null, null,
                null, cellId);
    }

    public static SlotType contructClickButton(ClickButtonType buttonClick) {
        return new SlotType(SlotEventEnum.ClickButton, null, buttonClick, null,
                null, -1);
    }

}
