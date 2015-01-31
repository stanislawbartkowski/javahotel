/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.factories;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.rdef.FormLineContainer;

public interface IDataModelFactory {

    IVModelData construct(IDataType dType);

    void copyFromPersistToModel(IDataType dType, IVModelData from,
            IVModelData to);

    void fromModelToPersist(IDataType dType, IVModelData from, IVModelData to);

    void fromViewToData(IDataType dType, FormLineContainer fContainer,
            IVModelData aTo);

    void fromDataToView(IDataType dType, PersistTypeEnum persistTypeEnum,
            IVModelData aFrom, FormLineContainer fContainer);
}
