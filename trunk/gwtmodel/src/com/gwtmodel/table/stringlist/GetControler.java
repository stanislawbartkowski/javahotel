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
package com.gwtmodel.table.stringlist;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryRecordPersist;
import com.gwtmodel.table.rdef.FormLineContainer;

class GetControler implements IGetViewControllerFactory {

    private final StringFactory sFactory;
    private final IDataModelFactory dFactory;
    private final DataViewModelFactory daFactory;
    private final IMemoryListModel mList;

    private GetControler(String fieldName, String title, IStringEFactory eFactory,
            IVField fie, DataViewModelFactory daFactory, IMemoryListModel mList) {
        sFactory = new StringFactory(fie, fieldName, title);
        dFactory = new DataFactory(eFactory);
        this.daFactory = daFactory;
        this.mList = mList;
    }

    @Override
    public IComposeController construct(ICallContext iContext) {
        ComposeControllerFactory coFactory =
                GwtGiniInjector.getI().getComposeControllerFactory();
        FormLineContainer fContainer = sFactory.construct(iContext.getDType());
        IComposeController iCon = coFactory.construct(iContext.getDType(), dFactory);
        IDataViewModel daModel = daFactory.construct(iContext.getDType(), fContainer,
                dFactory);
        ComposeControllerType cType = new ComposeControllerType(daModel, iContext.getDType(),
                0, 0);
        iCon.registerControler(cType);
        MemoryRecordPersist mRecord = new MemoryRecordPersist(iContext.getDType(), mList.getDataList());
        iCon.registerControler(new ComposeControllerType(mRecord, iContext.getDType()));
        iCon.registerControler(new ComposeControllerType(new ValidateS(iContext.getDType()),
                iContext.getDType()));
        return iCon;
    }
}
