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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

public class LoginViewFactory {

    private LoginViewFactory() {
    }

    public static FormLineContainer construct() {
        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        List<FormField> di = new ArrayList<FormField>();
        IFormLineView loginName = eFactory.constructTextField();
        di.add(new FormField("Symbol", loginName, new LoginField(
                LoginField.F.LOGINNAME)));
        IFormLineView password = eFactory.constructPasswordField();
        di.add(new FormField("Hasło", password, new LoginField(
                LoginField.F.PASSWORD)));
        return new FormLineContainer(di);
    }

    public static ILoginDataView contructView(int cellId, int firstId,
            IDataType dType, FormLineContainer lContainer,
            IDataModelFactory dFactory, IDataValidateAction vAction) {
        return new LoginDataView(cellId, firstId, dType, lContainer, dFactory,
                vAction);
    }

}
