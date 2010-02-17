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
package com.javahotel.client.dialog.login;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.login.LoginDataModelFactory;
import com.gwtmodel.table.login.LoginField;

class CustomLoginDataModelFactory extends LoginDataModelFactory {

    @Override
    public IVModelData construct(IDataType dType) {
        return new CustomLoginData();
    }

    private LoginField[] getLi() {
        LoginField[] li = { new LoginField(LoginField.F.LOGINNAME),
                new LoginField(LoginField.F.PASSWORD),
                new LoginField(LoginField.F.OTHER) };
        return li;
    }

    @Override
    public void copyFromPersistToModel(IDataType dType, IVModelData from,
            IVModelData to) {
        copyData(getLi(), from, to);
    }

    @Override
    public void fromModelToPersist(IDataType dType, IVModelData from,
            IVModelData to) {
        copyData(getLi(), from, to);
    }
}
