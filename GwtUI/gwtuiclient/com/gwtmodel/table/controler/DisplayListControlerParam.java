/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.table.IGetCellValue;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public class DisplayListControlerParam {

    private final TablesFactories tFactories;
    private final ITableCustomFactories fContainer;
    private final WSize wSize;
    private final CellId panelId;
    private final ListOfControlDesc listButton;
    private final ISlotable cControler;
    private final DataListParam listParam;
    private final ISlotMediator me;
    private final IGetCellValue getCell;
    private final boolean treeView;
    private final boolean selectRows;
    private final boolean asyncRow;

    DisplayListControlerParam(TablesFactories tFactories,
            ITableCustomFactories fContainer, WSize wSize, CellId panelId,
            ListOfControlDesc listButton, ISlotable cControler,
            DataListParam listParam, ISlotMediator me, IGetCellValue getCell,
            boolean treeView, boolean selectRows, boolean asyncRow) {
        this.tFactories = tFactories;
        this.fContainer = fContainer;
        this.wSize = wSize;
        this.panelId = panelId;
        this.listButton = listButton;
        this.cControler = cControler;
        this.listParam = listParam;
        this.me = me;
        this.getCell = getCell;
        this.treeView = treeView;
        this.selectRows = selectRows;
        this.asyncRow = asyncRow;
    }

    /**
     * @return the tFactories
     */
    TablesFactories gettFactories() {
        return tFactories;
    }

    /**
     * @return the fContainer
     */
    ITableCustomFactories getfContainer() {
        return fContainer;
    }

    /**
     * @return the dType
     */
    IDataType getdType() {
        return listParam.getdType();
    }

    /**
     * @return the wSize
     */
    WSize getwSize() {
        return wSize;
    }

    /**
     * @return the panelId
     */
    CellId getPanelId() {
        return panelId;
    }

    /**
     * @return the listButton
     */
    ListOfControlDesc getListButton() {
        return listButton;
    }

    /**
     * @return the cControler
     */
    ISlotable getcControler() {
        return cControler;
    }

    /**
     * @return the listParam
     */
    DataListParam getListParam() {
        return listParam;
    }

    /**
     * @return the me
     */
    ISlotMediator getMe() {
        return me;
    }

    /**
     * @return the getCell
     */
    IGetCellValue getGetCell() {
        return getCell;
    }

    /**
     * @return the treeView
     */
    public boolean isTreeView() {
        return treeView;
    }

    /**
     * @return the selectRows
     */
    public boolean isSelectRows() {
        return selectRows;
    }

    boolean isAsyncRow() {
        return asyncRow;
    }

}
