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
package com.gwtmodel.table.controler;

import com.google.gwt.user.client.Window;
import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotType;

class DataListCrudControler extends AbstractSlotContainer {

    private final IDataType dType;
    
    private class AddItem implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            Window.alert("add");
        }

    }

    private class RemoveItem implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            Window.alert("remove");
        }

    }

    private class ModifItem implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            ISlotSignalContext ret = callGetterList(dType);
            DataListType dList = ret.getDataList();
            Window.alert("modif");
        }

    }

    DataListCrudControler(IDataType dType) {
        this.dType = dType;
        SlotType slType = slTypeFactory
                .contructClickButton(ClickButtonType.StandClickEnum.ADDITEM);
        addSubscriber(slType, new AddItem());
        slType = slTypeFactory
                .contructClickButton(ClickButtonType.StandClickEnum.REMOVEITEM);
        addSubscriber(slType, new RemoveItem());
        slType = slTypeFactory
                .contructClickButton(ClickButtonType.StandClickEnum.MODIFITEM);
        addSubscriber(slType, new ModifItem());
    }

    public void startPublish() {
    }

}
