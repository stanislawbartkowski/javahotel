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

import com.google.inject.Inject;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateActionFactory;
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.AbstractToFactory;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.persistrecord.PersistRecordFactory;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.nmvc.dataviewmodel.GetViewFactory;
import com.javahotel.nmvc.factories.impl.RecordFormDefFactory;

public class RegisterFactories {

    private final GetRecordDefFactory gFactory;
    private final ITableAbstractFactories tFactories;
    private final ColListFactory cFactory;
    private final IResLocator rI;
    private final AbstractToFactory aFactory;
    private final DictValidatorFactory valFactory;
    private final RecordFormDefFactory dFactory;

    @Inject
    public RegisterFactories(IResLocator rI, GetRecordDefFactory gFactory,
            ColListFactory cFactory, AbstractToFactory aFactory,
            DictValidatorFactory valFactory, RecordFormDefFactory dFactory) {
        this.rI = rI;
        this.gFactory = gFactory;
        this.tFactories = GwtGiniInjector.getI().getITableAbstractFactories();
        this.cFactory = cFactory;
        this.aFactory = aFactory;
        this.valFactory = valFactory;
        this.dFactory = dFactory;
    }

    public void register() {
        FormDefFactory fa = new FormDefFactory(gFactory, dFactory);
        IDataModelFactory daFactory = new DataModelFactory(aFactory);
        PersistFactoryAction peFactory = new PersistFactoryAction(rI);
        IDataValidateActionFactory vFactory = new ValidateActionFactory(
                valFactory, daFactory);
        GetViewFactory getViewFactory = new GetViewFactory(fa, vFactory,
                peFactory);
        tFactories.registerFormDefFactory(fa);
        tFactories.registerHeaderListFactory(new HeaderListFactory(cFactory));
        tFactories.registerPersistFactory(peFactory);
        tFactories.registerDataModelFactory(daFactory);
        tFactories.registerDataValidateActionFactory(vFactory);
        tFactories.registerGetViewControllerFactory(getViewFactory);
        tFactories.registerGetCustomValues(new CustomFactory());
        tFactories
                .registerDataFormConstructorAbstractFactory(new FormFactory());
    }

}
