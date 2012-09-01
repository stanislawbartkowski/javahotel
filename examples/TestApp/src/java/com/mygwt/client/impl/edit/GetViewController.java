/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.mygwt.client.impl.edit;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

/**
 * @author hotel
 * 
 */
class GetViewController implements IGetViewControllerFactory {

    private final ComposeControllerFactory fFactory;
    private final IDataModelFactory dFactory;

    @Override
    public IComposeController construct(ICallContext iContext) {
        IComposeController i = fFactory
                .construct(iContext.getDType(), dFactory);
        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        List<FormField> di = new ArrayList<FormField>();
        List<VListHeaderDesc> vList = HeaderList.getHList();
        for (VListHeaderDesc v : vList) {
            IFormLineView eLine = eFactory.constructEditWidget(v.getFie());
            di.add(new FormField(v.getHeaderString(), eLine));

        }
        FormLineContainer fContainer = new FormLineContainer(di);
        DataViewModelFactory daFactory = GwtGiniInjector.getI()
                .getDataViewModelFactory();

        IDataViewModel daModel = daFactory.construct(iContext.getDType(),
                fContainer, dFactory);
        ComposeControllerType cType = new ComposeControllerType(daModel,
                iContext.getDType(), 0, 0);
        i.registerControler(cType);

        IDataValidateAction iValidate = new ValidateAction(iContext.getDType());
        cType = new ComposeControllerType(iValidate, iContext.getDType());
        i.registerControler(cType);

        IDataPersistAction iPersist = new ItemDataPersistAction(
                iContext.getDType());

        cType = new ComposeControllerType(iPersist, iContext.getDType());
        i.registerControler(cType);

        return i;
    }

    public GetViewController(IDataModelFactory dFactory) {
        fFactory = GwtGiniInjector.getI().getComposeControllerFactory();
        this.dFactory = dFactory;
    }

}
