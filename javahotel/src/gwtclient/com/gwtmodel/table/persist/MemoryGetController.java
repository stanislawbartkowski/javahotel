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
package com.gwtmodel.table.persist;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormLineContainer;

/**
 *
 * @author hotel
 */
public class MemoryGetController implements IGetViewControllerFactory {

    private final IFormDefFactory defFactory;
    private final IDataModelFactory dFactory;
    private final DataViewModelFactory daFactory;
    private final IMemoryListModel mList;
    private final IDataValidateActionFactory vFactory;

    public MemoryGetController(IFormDefFactory defFactory,
            IDataModelFactory dFactory, DataViewModelFactory daFactory,
            IMemoryListModel mList, IDataValidateActionFactory vFactory) {
        this.defFactory = defFactory;
        this.dFactory = dFactory;
        this.daFactory = daFactory;
        this.mList = mList;
        this.vFactory = vFactory;
    }

    @Override
    public IComposeController construct(IDataType dType) {
        ComposeControllerFactory coFactory =
                GwtGiniInjector.getI().getComposeControllerFactory();
        FormLineContainer fContainer = defFactory.construct(dType);
        IComposeController iCon = coFactory.construct(dType, dFactory);
        IDataViewModel daModel = daFactory.construct(dType, fContainer,
                dFactory);
        ComposeControllerType cType = new ComposeControllerType(daModel, dType,
                0, 0);
        iCon.registerControler(cType);
        MemoryRecordPersist mRecord = new MemoryRecordPersist(dType, mList.getDataList());
        iCon.registerControler(new ComposeControllerType(mRecord, dType));
        iCon.registerControler(new ComposeControllerType(vFactory.construct(dType), dType));
        return iCon;
    }
}
