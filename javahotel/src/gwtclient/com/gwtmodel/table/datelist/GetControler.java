/*
 *  Copyright 2010 stanislawbartkowski@gmail.com
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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.persist.IMemoryListModel;
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

    GetControler(String title, IDatePeriodFactory eFactory,
            DataViewModelFactory daFactory, IMemoryListModel dList) {
        sFactory = new DateViewFactory(title);
        dFactory = new DataFactory(eFactory);
        this.daFactory = daFactory;
        this.dList = dList;
    }

    @Override
    public IComposeController construct(IDataType dType) {
        ComposeControllerFactory coFactory = GwtGiniInjector.getI().getComposeControllerFactory();
//        FormLineContainer fContainer = sFactory.construct(dType);
        IComposeController iCon = coFactory.construct(dType, dFactory);
 //      IDataViewModel daModel = daFactory.construct(dType, fContainer,
 //               dFactory);
        IDataViewModel daModel = daFactory.construct(dType, null,
                dFactory);
        ComposeControllerType cType = new ComposeControllerType(daModel, dType,
                0, 0);
        iCon.registerControler(cType);
        iCon.registerControler(new ComposeControllerType(dList, dType));
        iCon.registerControler(new ComposeControllerType(new ValidateS(dType),
                dType));
        return iCon;
    }
}
