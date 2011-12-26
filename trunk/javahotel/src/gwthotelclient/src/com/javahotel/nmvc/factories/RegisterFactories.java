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
package com.javahotel.nmvc.factories;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.javahotel.client.injector.HInjector;
import com.javahotel.nmvc.factories.persist.DataPersistLayer;
import com.javahotel.nmvc.factories.validate.ValidateAction;

public class RegisterFactories {

    private final ITableAbstractFactories tFactories;
    private final IFormDefFactory rFactory;

    @Inject
    public RegisterFactories(IFormDefFactory rFactory) {
        this.tFactories = GwtGiniInjector.getI().getITableAbstractFactories();
        this.rFactory = rFactory;
    }

    public void register() {

        IDataValidateActionFactory valFactory = new IDataValidateActionFactory() {

            @Override
            public IDataValidateAction construct(IDataType dType) {
                return new ValidateAction(dType);
            }

        };

        // IPersistFactoryAction peFactory = new IPersistFactoryAction() {
        //
        // @Override
        // public IDataPersistAction contruct(IDataType dType) {
        // return new DataPersistLayer(dType);
        // }
        // };
        IPersistFactoryAction peFactory = HInjector.getI()
                .getIPersistFactoryAction();
        IFormTitleFactory tiFactory = new RecordTitleFactory();
        tFactories.registerGetCustomValues(new CustomFactory());
        tFactories.registerFormTitleFactory(tiFactory);
        GetViewFactory getViewFactory = new GetViewFactory(valFactory,
                peFactory, rFactory, tiFactory);
        tFactories.registerGetViewControllerFactory(getViewFactory);
        tFactories
                .registerDataFormConstructorAbstractFactory(new FormFactory());
        tFactories.registerPersistFactory(peFactory);
        IDataModelFactory daFactory = new DataModelFactory();
        tFactories.registerDataModelFactory(daFactory);
        tFactories.registerDataValidateActionFactory(valFactory);
        tFactories.registerHeaderListFactory(new HeaderListFactory());
    }

}
