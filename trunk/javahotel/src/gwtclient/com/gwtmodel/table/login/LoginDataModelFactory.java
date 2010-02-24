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
package com.gwtmodel.table.login;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.view.util.FormUtil;

public class LoginDataModelFactory implements IDataModelFactory {

    public IVModelData construct(IDataType dType) {
        return new LoginData();
    }

    private LoginField[] getLi() {
        LoginField[] li = { new LoginField(LoginField.F.LOGINNAME),
                new LoginField(LoginField.F.PASSWORD) };
        return li;
    }

    protected void copyData(LoginField[] li, IVModelData from, IVModelData to) {
        for (int i = 0; i < li.length; i++) {
            LoginField f = li[i];
            String s = from.getS(f);
            to.setS(f, s);
        }
    }

    public void copyFromPersistToModel(IDataType dType, IVModelData from,
            IVModelData to) {
        copyData(getLi(), from, to);
    }

    public void fromDataToView(IDataType dType, IVModelData aFrom,
            FormLineContainer fContainer) {
        FormUtil.copyFromDataToView(aFrom, fContainer);
    }

    public void fromModelToPersist(IDataType dType, IVModelData from,
            IVModelData to) {
        copyData(getLi(), from, to);
    }

    public void fromViewToData(IDataType dType, FormLineContainer fContainer,
            IVModelData aTo) {
        FormUtil.copyFromViewToData(fContainer, aTo);
    }

}
