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

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.factories.IDataCrudModifButtonActionFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.factories.IHeaderListFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.CellId;

public class TableDataControlerFactory {

    private final TablesFactories tFactories;
    private final ITableCustomFactories fContainer;
    private final ControlButtonFactory cButtonFactory;

    @Inject
    public TableDataControlerFactory(TablesFactories tFactories,
            ITableCustomFactories fContainer,
            ControlButtonFactory cButtonFactory) {
        this.tFactories = tFactories;
        this.fContainer = fContainer;
        this.cButtonFactory = cButtonFactory;
    }

    private DataListParam getParam(IDataType dType) {
        IDataPersistAction persistA = fContainer.getPersistFactoryAction().contruct(dType);
        IHeaderListContainer heList = null;
        IHeaderListFactory hFa = fContainer.getHeaderListFactory();
        if (hFa != null) {
            heList = fContainer.getHeaderListFactory().construct(dType);
        }
        IDataModelFactory dataFactory = fContainer.getDataModelFactory();
        IFormTitleFactory formFactory = fContainer.getFormTitleFactory();
        IGetViewControllerFactory fControler = fContainer.getGetViewControllerFactory();
        IDataCrudModifButtonActionFactory modifButton = fContainer.getDataCrudModifButtonActionFactory();
        return new DataListParam(persistA, heList, dataFactory, formFactory, fControler, modifButton);
    }

    public IDataControler constructDataControler(DisplayListControlerParam cParam) {
        return new DisplayListControler(cParam);
    }

    public DisplayListControlerParam constructParam(IDataType dType,
            WSize wSize, CellId panelId) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        return constructParam(dType, wSize, cList, panelId);
    }

    public DisplayListControlerParam constructChooseParam(IDataType dType,
            WSize wSize, CellId panelId) {
        ListOfControlDesc cList = cButtonFactory.constructChooseList();
        return constructParam(dType, wSize, cList, panelId);
    }

    public DisplayListControlerParam constructParam(IDataType dType, CellId panelId,
            ISlotMediator me) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        DataListParam listParam = getParam(dType);
        return new DisplayListControlerParam(tFactories, fContainer, dType, null,
                panelId, cList, new DataListCrudControler(
                tFactories, fContainer, listParam, dType), listParam, me);
    }

    public DisplayListControlerParam constructParam(IDataType dType, ListOfControlDesc cList, CellId panelId,
            ISlotMediator me) {
        DataListParam listParam = getParam(dType);
        return new DisplayListControlerParam(tFactories, fContainer, dType, null,
                panelId, cList, new DataListCrudControler(
                tFactories, fContainer, listParam, dType), listParam, me);
    }

    public DisplayListControlerParam constructParam(IDataType dType, CellId panelId,
            DataListParam listParam, ISlotMediator me) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        return new DisplayListControlerParam(tFactories, fContainer, dType, null,
                panelId, cList, new DataListCrudControler(
                tFactories, fContainer, listParam, dType), listParam, null);
    }

    public DisplayListControlerParam constructParam(IDataType dType,
            WSize wSize, ListOfControlDesc cList, CellId panelId) {
        DataListParam listParam = getParam(dType);
        return new DisplayListControlerParam(tFactories, fContainer, dType, wSize,
                panelId, cList, new DataListCrudControler(
                tFactories, fContainer, listParam, dType), listParam, null);
    }
}
