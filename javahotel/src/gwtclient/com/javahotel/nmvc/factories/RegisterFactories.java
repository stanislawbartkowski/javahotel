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
import com.gwtmodel.table.factories.ITableAbstractFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.AbstractToFactory;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;

public class RegisterFactories {

    private final GetRecordDefFactory gFactory;
    private final ITableAbstractFactories tFactories;
    private final ColListFactory cFactory;
    private final IResLocator rI;
    private final AbstractToFactory aFactory;

    @Inject
    public RegisterFactories(IResLocator rI, GetRecordDefFactory gFactory,
            ColListFactory cFactory, AbstractToFactory aFactory) {
        this.rI = rI;
        this.gFactory = gFactory;
        this.tFactories = GwtGiniInjector.getI().getITableAbstractFactories();
        this.cFactory = cFactory;
        this.aFactory = aFactory;

    }

    public void register() {
        FormDefFactory fa = new FormDefFactory(gFactory);
        tFactories.registerFormDefFactory(fa);
        tFactories.registerHeaderListFactory(new HeaderListFactory(cFactory));
        tFactories.registerPersistFactory(new PersistFactoryAction(rI));
        tFactories.registerDataModelFactory(new DataModelFactory(aFactory));
        tFactories
                .registerDataValidateActionFactory(new ValidateActionFactory());
    }

}
