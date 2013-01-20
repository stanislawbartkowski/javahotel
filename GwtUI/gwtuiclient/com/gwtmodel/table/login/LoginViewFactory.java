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
package com.gwtmodel.table.login;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import java.util.ArrayList;
import java.util.List;

public class LoginViewFactory {

    private LoginViewFactory() {
    }

    public static FormLineContainer construct() {
        EditWidgetFactory eFactory = GwtGiniInjector.getI().
                getEditWidgetFactory();
        List<FormField> di = new ArrayList<FormField>();
        IGetCustomValues c = GwtGiniInjector.getI().getTableFactoriesContainer().getGetCustomValues();
        IVField loginV = new LoginField(LoginField.F.LOGINNAME);
        IFormLineView loginName = eFactory.constructTextField(loginV);
        di.add(new FormField(c.getCustomValue(c.LOGINMAME), loginName));
        IVField passwordV = new LoginField(LoginField.F.PASSWORD);
        IFormLineView password = eFactory.constructPasswordField(passwordV);
        di.add(new FormField(c.getCustomValue(c.PASSWORD), password));
        return new FormLineContainer(di);
    }

    public static ILoginDataView contructView(CellId cellId, IDataType dType,
            FormLineContainer lContainer,
            IDataModelFactory dFactory, IDataValidateAction vAction) {
        return new LoginDataView(cellId, dType, lContainer, dFactory,
                vAction);
    }
}
