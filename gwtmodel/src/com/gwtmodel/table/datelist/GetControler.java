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

import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.MemoryRecordPersist;
import com.gwtmodel.table.rdef.FormLineContainer;

/**
 * 
 * @author hotel
 */
class GetControler implements IGetViewControllerFactory {

    private final DateViewFactory sFactory;
    private final IDataModelFactory dFactory;
    private final DataViewModelFactory daFactory;
    private final IMemoryListModel dList;
    private final IDataValidateActionFactory vFactory;

    private GetControler(String title, IDatePeriodFactory eFactory,
            DataViewModelFactory daFactory, IMemoryListModel dList,
            IDataValidateActionFactory vFactory) {
        sFactory = new DateViewFactory(title);
        dFactory = new DataFactory(eFactory);
        this.daFactory = daFactory;
        this.dList = dList;
        this.vFactory = vFactory;
    }

    @Override
    public IComposeController construct(ICallContext iCall) {

        ComposeControllerFactory coFactory = GwtGiniInjector.getI()
                .getComposeControllerFactory();
        FormLineContainer fContainer = sFactory.construct(iCall.getDType());
        IComposeController iCon = coFactory.construct(iCall.getDType(),
                dFactory);
        IDataViewModel daModel = daFactory.construct(iCall.getDType(),
                fContainer, dFactory);
        ComposeControllerType cType = new ComposeControllerType(daModel,
                iCall.getDType(), 0, 0);
        iCon.registerControler(cType);
        MemoryRecordPersist mRecord = new MemoryRecordPersist(iCall.getDType(),
                dList.getDataList());
        iCon.registerControler(new ComposeControllerType(mRecord, iCall
                .getDType()));
        // iCon.registerControler(new ComposeControllerType(new ValidateS(iCall
        // .getDType()), iCall.getDType()));
        iCon.registerControler(new ComposeControllerType(vFactory
                .construct(iCall.getDType()), iCall.getDType()));
        return iCon;
    }
}
