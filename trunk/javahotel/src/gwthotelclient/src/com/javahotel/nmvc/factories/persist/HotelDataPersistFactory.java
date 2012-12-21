/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.persist;

import javax.inject.Inject;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.javahotel.client.IResLocator;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.DictType;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;

/**
 * @author hotel
 * 
 */
public class HotelDataPersistFactory implements IPersistFactoryAction {

    private final IResLocator rI;
    private final IHotelPersistFactory iPersistFactory;

    @Inject
    public HotelDataPersistFactory(IResLocator rI,
            IHotelPersistFactory iPersistFactory) {
        this.rI = rI;
        this.iPersistFactory = iPersistFactory;
    }

    @Override
    public IDataPersistAction contruct(IDataType dType) {
        DataType dd = (DataType) dType;
        if (dd.isDictType(DictType.InvoiceList)) {
            return new InvoicePersistLayer(dType, rI, iPersistFactory);
        }
        return new DataPersistLayer(dType, rI, iPersistFactory);
    }

    @Override
    public IDataPersistListAction constructL(IDataType dType) {
        return new DataPersistList(dType, rI);
    }

}
