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
package com.gwtmodel.table.stringlist;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;

class DataFactory implements IDataModelFactory {

    private final IStringEFactory eFactory;
    private final IVField fie;

    DataFactory(IStringEFactory eFactory, IVField fie) {
        this.eFactory = eFactory;
        this.fie = fie;
    }

    public IVModelData construct(IDataType dType) {
        return eFactory.construct(dType);
    }

    public void copyFromPersistToModel(IDataType dType, IVModelData from,
            IVModelData to) {
        AbstractStringE efrom = (AbstractStringE) from;
        AbstractStringE eto = (AbstractStringE) to;
        eto.setF(fie, efrom.getF(fie));
    }

    private IFormLineView getI(FormLineContainer fContainer) {
        FormField i = fContainer.getfList().get(0);
        return i.getELine();
    }

    public void fromModelToPersist(IDataType dType, IVModelData from,
            IVModelData to) {
        copyFromPersistToModel(dType, from, to);
    }

    public void fromViewToData(IDataType dType, FormLineContainer fContainer,
            IVModelData aTo) {
        AbstractStringE eto = (AbstractStringE) aTo;
        IFormLineView e = getI(fContainer);
        eto.setF(fie, e.getVal());
    }

    public void fromDataToView(IDataType dType, PersistTypeEnum persistTypeEnum, IVModelData aFrom, FormLineContainer fContainer) {
        AbstractStringE efrom = (AbstractStringE) aFrom;
        IFormLineView e = getI(fContainer);
        e.setVal(efrom.getS(fie));
    }

}
