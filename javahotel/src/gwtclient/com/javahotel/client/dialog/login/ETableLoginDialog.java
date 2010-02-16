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

import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.login.ILoginDataView;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.login.LoginDataModelFactory;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.login.LoginViewFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.util.StringU;
import com.javahotel.nmvc.common.FormLineDef;

public class ETableLoginDialog {

    private class LoginType implements IDataType {

        public boolean eq(IDataType dType) {
            return true;
        }
    }

    private class SetGwt implements ISlotSignaller {

        private final ISetGwtWidget iSet;

        SetGwt(ISetGwtWidget iSet) {
            this.iSet = iSet;
        }

        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            iSet.setGwtWidget(new DefaultMvcWidget(w.getGWidget()));
        }
    }

    private class CustomLoginData extends LoginData {

        private String hotel;

        @Override
        public String getS(IVField fie) {
            LoginField f = (LoginField) fie;
            if (f.getF() == LoginField.F.OTHER) {
                return hotel;
            }
            return super.getS(fie);
        }

        @Override
        public boolean isEmpty(IVField fie) {
            LoginField f = (LoginField) fie;
            if (f.getF() == LoginField.F.OTHER) {
                return StringU.isEmpty(hotel);
            }
            return super.isEmpty(fie);
        }

        @Override
        public void setS(IVField fie, String s) {
            LoginField f = (LoginField) fie;
            if (f.getF() == LoginField.F.OTHER) {
                hotel = s;
                return;
            }
            super.setS(fie, s);
        }
    }

    private class CustomLoginDataModelFactory extends LoginDataModelFactory {

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

    public ETableLoginDialog(final IResLocator rI, ISetGwtWidget iSet,
            final boolean user, final ICommand iNext) {
        FormLineContainer lContainer = LoginViewFactory.construct();
        if (user) {
            ILineField hotel = GetIEditFactory.getListValuesBox(rI,
                    RType.AllHotels, new CommandParam(), HotelP.F.name);
            FormField f = new FormField("Hotel", new FormLineDef(hotel), new LoginField(
                    LoginField.F.OTHER));
            lContainer.addFormField(f);
        }
        ILoginDataView dView = LoginViewFactory.contructView(0, 1,
                new LoginType(), lContainer, new CustomLoginDataModelFactory());
        dView.getSlContainer().registerSubscriber(0, new SetGwt(iSet));
        dView.startPublish(0);
    }
}
