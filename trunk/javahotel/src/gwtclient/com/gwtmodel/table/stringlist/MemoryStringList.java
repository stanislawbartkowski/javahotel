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
package com.gwtmodel.table.stringlist;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryListPersist;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;

public class MemoryStringList extends AbstractSlotContainer implements
        IMemoryStringList {

    private final IMemoryListModel lPhonelist;
    private final IMemoryListModel lPhonedata;
    private final IDataControler dControler;
    private final IDataType sType;
    private final String fieldName;
    private final String title;

    public void setMemTable(IDataListType dList) {
        lPhonelist.setDataList(dList);
        lPhonedata.setDataList(dList);
        dControler.startPublish(new CellId(0));
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        VListHeaderDesc he = new VListHeaderDesc(fieldName, Empty.getFieldType());
        heList.add(he);
        VListHeaderContainer vHeader;
        vHeader = new VListHeaderContainer(heList, title);
        dControler.getSlContainer().publish(sType, vHeader);
    }

    MemoryStringList(String fieldName, String title,
            IStringEFactory eFactory, ISlotSignaller setGwt) {
        TableDataControlerFactory tFactory = GwtGiniInjector.getI().getTableDataControlerFactory();
        DataViewModelFactory daFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        sType = Empty.getDataType();
        this.fieldName = fieldName;
        this.title = title;
        lPhonelist = new MemoryListPersist(sType);
        lPhonedata = new MemoryListPersist(sType);

        IVField sField = Empty.getFieldType();

//        dControler = tFactory.constructDataControler(sType, new CellId(0),
//                new DataListParam(lPhonelist, null, new DataFactory(eFactory,
//                sField), new StringFactory(fieldName, title),
//                new GetControler(fieldName, title, eFactory, sField,
//                daFactory, lPhonedata)), null);
        DisplayListControlerParam cParam = tFactory.constructParam(sType, new CellId(0),
                new DataListParam(lPhonelist, null, new DataFactory(eFactory,
                sField), new StringFactory(fieldName, title),
                new GetControler(fieldName, title, eFactory, sField,
                daFactory, lPhonedata)), null);
        dControler = tFactory.constructDataControler(cParam);
        dControler.getSlContainer().registerSubscriber(0, setGwt);
    }

    public void startPublish(CellId cellId) {
    }

    public IDataListType getMemTable() {
        return lPhonelist.getDataList();
    }
}
