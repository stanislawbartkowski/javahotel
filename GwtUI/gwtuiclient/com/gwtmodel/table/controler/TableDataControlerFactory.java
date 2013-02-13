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

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.factories.*;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.table.IGetCellValue;

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
        IDataPersistListAction persistA = fContainer.getPersistFactoryAction()
                .constructL(dType);
        IHeaderListContainer heList = null;
        IHeaderListFactory hFa = fContainer.getHeaderListFactory();
        if (hFa != null) {
            heList = fContainer.getHeaderListFactory().construct(dType);
        }
        IDataModelFactory dataFactory = fContainer.getDataModelFactory();
        IFormTitleFactory formFactory = fContainer.getFormTitleFactory();
        IGetViewControllerFactory fControler = fContainer
                .getGetViewControllerFactory();
        IDataCrudModifButtonActionFactory modifButton = fContainer
                .getDataCrudModifButtonActionFactory();
        return new DataListParam(dType, persistA, heList, dataFactory,
                formFactory, fControler, modifButton);
    }

    public IDataControler constructDataControler(
            DisplayListControlerParam cParam) {
        return new DisplayListControler(cParam);
    }

    /**
     * Construct ISlotable for single record with buttons and life cycle (do not
     * run startPublish)
     * 
     * @param cParam
     *            DisplayListControlerParam
     * @param action
     *            Action parameters like ADD, SHOW
     * @param vData
     *            IVModelData to use
     * @param wSize
     *            WSize position on the screen
     * @return ISlotable
     */
    public ISlotable constructDataControler(DisplayListControlerParam cParam,
            ClickButtonType.StandClickEnum action, IVModelData vData,
            WSize wSize) {
        return new DisplayBoxControler().construct(cParam, action, vData,
                wSize, false);
    }

    /**
     * Construct ISlotable for single record without button, content only. It is
     * a responsibility of the caller to position dialog on the screen and
     * should run startPublish
     * 
     * @param cParam
     *            DisplayListControlerParam
     * @param action
     *            Action parameters like ADD, SHOW
     * @param vData
     *            IVModelData to use
     * @return ISlotable
     */
    public ISlotable constructDataControler(DisplayListControlerParam cParam,
            ClickButtonType.StandClickEnum action, IVModelData vData) {
        return new DisplayBoxControler().construct(cParam, action, vData, null,
                true);
    }

    public DisplayListControlerParam constructParam(IDataType dType,
            WSize wSize, CellId panelId) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        return constructParam(dType, wSize, cList, panelId);
    }

    public DisplayListControlerParam constructChooseParam(IDataType dType,
            WSize wSize, CellId panelId, String htmlFormat) {
        ListOfControlDesc cList = cButtonFactory
                .constructChooseList(htmlFormat);
        return constructParam(dType, wSize, cList, panelId);
    }

    public DisplayListControlerParam constructParam(IDataType dType,
            CellId panelId, ISlotMediator me, boolean toTree,
            boolean selectRows, boolean asyncRow) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        DataListParam listParam = getParam(dType);
        return new DisplayListControlerParam(tFactories, fContainer, null,
                panelId, cList, new DataListCrudControler(tFactories,
                        fContainer, listParam), listParam, me, null, toTree,
                selectRows, asyncRow);
    }

    public DisplayListControlerParam constructParam(IDataType dType,
            ListOfControlDesc cList, CellId panelId, ISlotMediator me,
            IGetCellValue getCell, boolean toTree, boolean selectRows,
            boolean asyncRow) {
        DataListParam listParam = getParam(dType);
        return new DisplayListControlerParam(tFactories, fContainer, null,
                panelId, cList, new DataListCrudControler(tFactories,
                        fContainer, listParam), listParam, me, getCell, toTree,
                selectRows, asyncRow);
    }

    public DisplayListControlerParam constructParam(CellId panelId,
            DataListParam listParam, ISlotMediator me, IGetCellValue getCell,
            boolean treeView, boolean asyncRow) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        return new DisplayListControlerParam(tFactories, fContainer, null,
                panelId, cList, new DataListCrudControler(tFactories,
                        fContainer, listParam), listParam, me, getCell,
                treeView, true, asyncRow);
    }

    public DisplayListControlerParam constructParam(CellId panelId,
            DataListParam listParam, ISlotable iSlo) {
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        return new DisplayListControlerParam(tFactories, fContainer, null,
                panelId, cList, iSlo, listParam, null, null, false, true, false);
    }

    public DisplayListControlerParam constructParam(IDataType dType) {
        DataListParam listParam = getParam(dType);
        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        return new DisplayListControlerParam(tFactories, fContainer, null,
                null, cList, new DataListCrudControler(tFactories, fContainer,
                        listParam), listParam, null, null, false, true, false);
    }

    public DisplayListControlerParam constructParam(ListOfControlDesc cList,
            CellId panelId, DataListParam listParam, ISlotMediator me,
            boolean selectRows, boolean asyncRow) {
        return new DisplayListControlerParam(tFactories, fContainer, null,
                panelId, cList, new DataListCrudControler(tFactories,
                        fContainer, listParam), listParam, null, null, false,
                selectRows, asyncRow);
    }

    public DisplayListControlerParam constructParam(IDataType dType,
            WSize wSize, ListOfControlDesc cList, CellId panelId) {
        DataListParam listParam = getParam(dType);
        return new DisplayListControlerParam(tFactories, fContainer, wSize,
                panelId, cList, new DataListCrudControler(tFactories,
                        fContainer, listParam), listParam, null, null, false,
                true, false);
    }
}
