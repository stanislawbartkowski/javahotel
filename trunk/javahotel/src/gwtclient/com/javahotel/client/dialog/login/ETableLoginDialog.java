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
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.login.ILoginDataView;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.login.LoginViewFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.injector.HInjector;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.nmvc.ewidget.EWidgetFactory;

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

    private class Valid implements ISlotSignaller {

        final ICommand iNext;

        Valid(final ICommand iNext) {
            this.iNext = iNext;
        }

        public void signal(ISlotSignalContext slContext) {
            iNext.execute();
        }

    }

    public ETableLoginDialog(ISetGwtWidget iSet, final boolean user,
            final ICommand iNext) {
        FormLineContainer lContainer = LoginViewFactory.construct();
        IDataType dType = new LoginType();
        if (user) {
            EWidgetFactory eFactory = HInjector.getI().getEWidgetFactory();
            IFormLineView hotel = eFactory.getListValuesBox(RType.AllHotels,
                    new CommandParam(), HotelP.F.name);
            FormField f = new FormField("Hotel", hotel, new LoginField(
                    LoginField.F.OTHER));
            lContainer.addFormField(f);
        }

        IDataValidateAction vAction = new ValidateLogin(dType, user, lContainer);
        ILoginDataView dView = LoginViewFactory.contructView(new CellId(0), dType,
                lContainer, new CustomLoginDataModelFactory(), vAction);
        dView.getSlContainer().registerSubscriber(0, new SetGwt(iSet));
        dView.getSlContainer().registerSubscriber(DataActionEnum.ValidSignal,
                dType, new Valid(iNext));
        dView.startPublish(new CellId(0));
    }
}
