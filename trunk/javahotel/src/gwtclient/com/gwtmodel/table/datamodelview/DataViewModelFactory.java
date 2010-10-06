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
package com.gwtmodel.table.datamodelview;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.view.form.GwtFormViewFactory;

public class DataViewModelFactory {

    private final GwtFormViewFactory gFactory;
    private final ITableCustomFactories cFactories;

    @Inject
    public DataViewModelFactory(GwtFormViewFactory gFactory,
            ITableCustomFactories cFactories) {
        this.gFactory = gFactory;
        this.cFactories = cFactories;
    }

    public IDataViewModel construct(IDataType dType,
            FormLineContainer fContainer, IDataModelFactory dFactory) {
        return new DataViewModel(gFactory, dType, fContainer, cFactories,
                dFactory);
    }

    public IDataViewModel construct(IDataType dType,
            FormLineContainer fContainer) {
        return new DataViewModel(gFactory, dType, fContainer, cFactories,
                cFactories.getDataModelFactory());
    }
}
