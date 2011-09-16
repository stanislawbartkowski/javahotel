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
package com.gwtmodel.table.controler;

import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;

class DataListCrudControler extends AbstractSlotContainer {

    private final SlotSignalContextFactory slFactory;
    private final DataListActionItemFactory aFactory;
    private final FindListActionFactory fFactory;

    DataListCrudControler(TablesFactories tFactories,
            ITableCustomFactories fContainer, DataListParam listParam) {
        this.dType = listParam.getdType();
        assert dType != null : LogT.getT().cannotBeNull();
        this.slFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        aFactory = new DataListActionItemFactory(tFactories, dType, this,
                listParam, slFactory);
        fFactory = new FindListActionFactory(tFactories, dType, listParam);
        registerSubscriber(dType, DataActionEnum.ReadHeaderContainerSignal,
                fFactory.constructActionHeader());

        registerSubscriber(ClickButtonType.StandClickEnum.ADDITEM,
                aFactory.constructActionItem(PersistTypeEnum.ADD));
        registerSubscriber(ClickButtonType.StandClickEnum.REMOVEITEM,
                aFactory.constructActionItem(PersistTypeEnum.REMOVE));
        registerSubscriber(ClickButtonType.StandClickEnum.MODIFITEM,
                aFactory.constructActionItem(PersistTypeEnum.MODIF));
        registerSubscriber(ClickButtonType.StandClickEnum.SHOWITEM,
                aFactory.constructActionItem(PersistTypeEnum.SHOWONLY));
        registerSubscriber(ClickButtonType.StandClickEnum.FILTRLIST,
                fFactory.constructActionFind(
                        ClickButtonType.StandClickEnum.FILTRLIST, this, dType));
        registerSubscriber(ClickButtonType.StandClickEnum.FIND,
                fFactory.constructActionFind(
                        ClickButtonType.StandClickEnum.FIND, this, dType));
        registerSubscriber(dType, DataActionEnum.ChangeViewFormModeAction,
                aFactory.constructChangeMode());

    }
}
