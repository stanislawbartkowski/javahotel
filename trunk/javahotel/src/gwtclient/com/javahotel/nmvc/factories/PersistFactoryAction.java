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
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.persistrecord.PersistRecordFactory;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.persist.DataPersistLayer;

class PersistFactoryAction implements IPersistFactoryAction {

    private final IResLocator rI;
    private final PersistRecordFactory pFactory;
    private final IDataModelFactory gFactory;

    PersistFactoryAction(IResLocator rI, PersistRecordFactory pFactory,IDataModelFactory gFactory) {
        this.rI = rI;
        this.pFactory = pFactory;
        this.gFactory = gFactory;
    }

    public IDataPersistAction contruct(IDataType dType) {
        DataType dd = (DataType) dType;
        return new DataPersistLayer(rI,pFactory, gFactory, dd);

    }
}
