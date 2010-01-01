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
package com.javahotel.nmvc.factories;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormLineContainer;

class GetViewFactory extends HelperFactory implements IGetViewControllerFactory {

    private final FormDefFactory fFactory;
    private final ComposeControllerFactory coFactory;
    private final DataViewModelFactory daFactory;
    private final IDataValidateActionFactory vFactory;
    private final PersistFactoryAction peFactory;

    GetViewFactory(FormDefFactory fFactory,
            IDataValidateActionFactory vFactory, PersistFactoryAction peFactory) {
        this.fFactory = fFactory;
        coFactory = GwtGiniInjector.getI().getComposeControllerFactory();
        daFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        this.peFactory = peFactory;
        this.vFactory = vFactory;

    }

    public IComposeController construct(IDataType dType) {
        FormLineContainer fContainer = fFactory.construct(dType);
        IComposeController iCon = coFactory.construct(dType);
        IDataViewModel daModel = daFactory.construct(dType, fContainer);
        IDataValidateAction vAction = vFactory.construct(dType);
        IDataPersistAction persistA = peFactory.contruct(dType);

        ComposeControllerType cType = new ComposeControllerType(daModel, dType,
                0, 0);
        iCon.registerController(cType);
        iCon.registerController(new ComposeControllerType(vAction, dType));
        iCon.registerController(new ComposeControllerType(persistA, dType));
        return iCon;
    }

}
