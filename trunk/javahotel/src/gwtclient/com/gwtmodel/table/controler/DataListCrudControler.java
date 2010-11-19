/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;

class DataListCrudControler extends AbstractSlotContainer {

    private final SlotSignalContextFactory slFactory;
    private final DataListActionItemFactory aFactory;

    DataListCrudControler(TablesFactories tFactories,
            ITableCustomFactories fContainer, DataListParam listParam,
            IDataType dType) {
        assert dType != null : "Cannot be null";
        this.dType = dType;
        this.slFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        aFactory = new DataListActionItemFactory(tFactories, dType, this, listParam,
                slFactory);
        registerSubscriber(ClickButtonType.StandClickEnum.ADDITEM,
                aFactory.constructActionItem(PersistTypeEnum.ADD));

        registerSubscriber(ClickButtonType.StandClickEnum.REMOVEITEM,
                aFactory.constructActionItem(PersistTypeEnum.REMOVE));
        registerSubscriber(ClickButtonType.StandClickEnum.MODIFITEM,
                aFactory.constructActionItem(PersistTypeEnum.MODIF));
        registerSubscriber(ClickButtonType.StandClickEnum.SHOWITEM,
                aFactory.constructActionItem(PersistTypeEnum.SHOWONLY));
    }
}
