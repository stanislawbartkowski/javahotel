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
package com.javahotel.nmvc.factories;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.view.util.FormUtil;
import com.javahotel.client.abstractto.IAbstractFactory;
import com.javahotel.client.abstractto.InvoicePVData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;

class DataModelFactory implements IDataModelFactory {

    @Override
    public IVModelData construct(IDataType dType) {
        return DataUtil.construct(dType);
    }

    @Override
    public void copyFromPersistToModel(IDataType dType, IVModelData from,
            IVModelData to) {
        FormUtil.copyData(from, to);
    }

    @Override
    public void fromModelToPersist(IDataType dType, IVModelData from,
            IVModelData to) {
        copyFromPersistToModel(dType, from, to);
    }

    @Override
    public void fromDataToView(IDataType dType,
            PersistTypeEnum persistTypeEnum, IVModelData aFrom,
            FormLineContainer fContainer) {
        FormUtil.copyFromDataToView(aFrom, fContainer);
    }

    @Override
    public void fromViewToData(IDataType dType, FormLineContainer fContainer,
            IVModelData aTo) {
        FormUtil.copyFromViewToData(fContainer, aTo);
    }

}
