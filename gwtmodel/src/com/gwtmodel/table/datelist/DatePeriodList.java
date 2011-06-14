/*
 *  Copyright 2011 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.datelist;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryGetController;
import com.gwtmodel.table.persist.MemoryListPersist;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hotel
 */
class DatePeriodList extends AbstractSlotContainer implements IDatePeriodList {

    private final IMemoryListModel mList;
    private final String title;
    private final IDataControler dControler;

    DatePeriodList(String title, IDatePeriodFactory eFactory,
            ISlotSignaller setGwt, IDataValidateActionFactory vFactory) {
        this.title = title;
        this.dType = Empty.getDataType();

        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        DataViewModelFactory daFactory = GwtGiniInjector.getI()
                .getDataViewModelFactory();
        mList = new MemoryListPersist(dType);
        IGetViewControllerFactory iGetCon = new MemoryGetController(
                new DateViewFactory(title), new DataFactory(eFactory),
                daFactory, mList, vFactory);

        DisplayListControlerParam cParam = tFactory.constructParam(dType,
                new CellId(0), new DataListParam(mList, null, new DataFactory(
                        eFactory), new DateViewFactory(title),
                // new GetControler(title, eFactory, daFactory, mList)
                        iGetCon), null);
        dControler = tFactory.constructDataControler(cParam);
        dControler.getSlContainer().registerSubscriber(dType, 0, setGwt);
    }

    @Override
    public void setMemTable(IDataListType dList) {
        mList.setDataList(dList);
        dControler.startPublish(new CellId(0));
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        VListHeaderDesc he = new VListHeaderDesc("Od", new DatePeriodField(
                DatePeriodField.F.DATEFROM));
        heList.add(he);
        he = new VListHeaderDesc("Do", new DatePeriodField(
                DatePeriodField.F.DATETO));
        heList.add(he);
        he = new VListHeaderDesc("Opis", new DatePeriodField(
                DatePeriodField.F.COMMENT));
        heList.add(he);
        VListHeaderContainer vHeader;
        vHeader = new VListHeaderContainer(heList, title, 0, null);
        dControler.getSlContainer().publish(dType, vHeader);
    }

    @Override
    public IDataListType getMemTable() {
        return mList.getDataList();
    }
}
