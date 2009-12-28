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
package com.gwtmodel.table.datamodelview;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.view.form.GwtFormViewFactory;

public class DataViewModelFactory {

    private final GwtFormViewFactory gFactory;
    private final TableFactoriesContainer tContainer;

    @Inject
    public DataViewModelFactory(TableFactoriesContainer fContainer, GwtFormViewFactory gFactory) {
        this.gFactory = gFactory;
        this.tContainer = fContainer;
    }

    public IDataViewModel construct(IDataType dType, int cellId,
            FormLineContainer fContainer) {
        return new DataViewModel(tContainer, gFactory, dType, cellId, fContainer);
    }

}
