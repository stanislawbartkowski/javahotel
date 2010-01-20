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
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.javahotel.client.abstractto.AbstractToFactory;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.record.view.helper.ExtractFields;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.common.FormLineDef;
import com.javahotel.nmvc.common.VField;
import com.javahotel.nmvc.common.VModelData;
import com.javahotel.nmvc.factories.impl.DataModelFields;

class DataModelFactory extends HelperFactory implements IDataModelFactory {

    private final AbstractToFactory aFactory;

    DataModelFactory(AbstractToFactory aFactory) {
        this.aFactory = aFactory;
    }

    public IVModelData construct(IDataType dType) {
        DictData da = getDa(dType);
        AbstractTo a = aFactory.getA(da);
        return new VModelData(a);
    }

    public void copyFromPersistToModel(IDataType dType, IVModelData from,
            IVModelData to) {
        VModelData vFrom = (VModelData) from;
        VModelData vTo = (VModelData) to;
        AbstractTo aFrom = vFrom.getA();
        AbstractTo aTo = vTo.getA();
        aTo.copyFrom(aFrom);
    }

    public void fromModelToPersist(IDataType dType, IVModelData from,
            IVModelData to) {
        copyFromPersistToModel(dType, from, to);
    }

    public void fromDataToView(IVModelData aFrom, FormLineContainer fContainer) {
        for (FormField d : fContainer.getfList()) {
            String s = aFrom.getS(d.getFie());
            d.getELine().setVal(s);
        }

    }

    public void fromViewToData(FormLineContainer fContainer, IVModelData aTo) {
        VModelData vData = (VModelData) aTo;
        AbstractTo a = vData.getA();
        for (FormField d : fContainer.getfList()) {
            VField vFie = (VField) d.getFie();
            IFormLineView vView = d.getELine();
            if (vView instanceof FormLineDef) {
                FormLineDef vDef = (FormLineDef) vView;
                ExtractFields.fromViewToA(vFie.getFie(), vDef.getiField(), a);
            } else {
                DataModelFields.fromViewToA(vFie.getFie(), vView, a);
            }
        }

    }

}
