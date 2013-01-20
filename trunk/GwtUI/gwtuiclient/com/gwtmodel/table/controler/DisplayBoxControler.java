/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;

/**
 * @author hotel
 * 
 */
class DisplayBoxControler {

    private final SlotSignalContextFactory slFactory;
    private final TablesFactories tFactories;

    DisplayBoxControler() {
        this.slFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        this.tFactories = GwtGiniInjector.getI().getTablesFactories();
    }

    ISlotable construct(DisplayListControlerParam cParam,
            ClickButtonType.StandClickEnum action, IVModelData vData,
            WSize wSize, boolean contentonly) {
        IDataType dType = cParam.getdType();
        DataListActionItemFactory aFactory = new DataListActionItemFactory(
                tFactories, dType, cParam.getcControler(), cParam.getListParam(), slFactory);
        PersistTypeEnum persistTypeEnum = PersistTypeEnum.SHOWONLY;
        switch (action) {
        case SHOWITEM:
            break;
        case ADDITEM:
            persistTypeEnum = PersistTypeEnum.ADD;
            break;
        case MODIFITEM:
            persistTypeEnum = PersistTypeEnum.MODIF;
            break;
        case REMOVEITEM:
            persistTypeEnum = PersistTypeEnum.REMOVE;
            break;
        }
        return aFactory.BoxActionItem(action, persistTypeEnum, vData, wSize,
                contentonly);
    }

}
