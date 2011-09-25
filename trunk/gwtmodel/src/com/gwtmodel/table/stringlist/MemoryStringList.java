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
package com.gwtmodel.table.stringlist;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDataValidateAction;
import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryGetController;
import com.gwtmodel.table.persist.MemoryListPersist;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;

class MemoryStringList extends AbstractSlotMediatorContainer implements
        IMemoryStringList {

    private final IMemoryListModel lPersistList;
    private final IDataControler dControler;
    private final String fieldName;
    private final String title;
    private final IVField vString;

    @Override
    public void setMemTable(IDataListType dList) {
        lPersistList.setDataList(dList);
        dControler.startPublish(new CellId(0));
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        VListHeaderDesc he = new VListHeaderDesc(fieldName, vString);
        heList.add(he);
        VListHeaderContainer vHeader;
        vHeader = new VListHeaderContainer(heList, title, 0, null);
        dControler.getSlContainer().publish(dType, vHeader);
    }

    MemoryStringList(String fieldName, String title, IDataType dType,
            IVField vString, IStringEFactory eFactory, ISlotListener setGwt) {
        this.vString = vString;
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        DataViewModelFactory daFactory = GwtGiniInjector.getI()
                .getDataViewModelFactory();
        this.dType = dType;
        this.fieldName = fieldName;
        this.title = title;
        lPersistList = new MemoryListPersist(dType);

        IDataValidateActionFactory vFactory = new IDataValidateActionFactory() {

            @Override
            public IDataValidateAction construct(IDataType dType) {
                return new ValidateS(dType);
            }
        };

        IGetViewControllerFactory iGetCon = new MemoryGetController(
                new StringFactory(vString, fieldName, title), new DataFactory(
                        eFactory), daFactory, lPersistList, vFactory);

        DisplayListControlerParam cParam = tFactory.constructParam(
                new CellId(0), new DataListParam(dType, lPersistList, null,
                        new DataFactory(eFactory), new StringFactory(vString,
                                fieldName, title), iGetCon), null);
        dControler = tFactory.constructDataControler(cParam);
        dControler.getSlContainer().registerSubscriber(dType, 0, setGwt);
        slMediator.registerSlotContainer(dControler);
    }

    @Override
    public void startPublish(CellId cellId) {
    }

    @Override
    public IDataListType getMemTable() {
        return lPersistList.getDataList();
    }
}
