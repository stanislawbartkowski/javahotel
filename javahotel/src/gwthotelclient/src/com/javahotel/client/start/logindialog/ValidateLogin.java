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
package com.javahotel.client.start.logindialog;

import java.util.List;

import com.gwtmodel.table.IBackValidate;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.util.FormUtil;

class ValidateLogin extends AbstractSlotContainer implements
        IDataValidateAction {

    private final boolean user;
    private final FormLineContainer fContainer;

    ValidateLogin(IDataType dType, boolean user, FormLineContainer fContainer) {
        this.dType = dType;
        this.user = user;
        this.fContainer = fContainer;
        registerSubscriber(dType, DataActionEnum.ValidateAction,
                new ValidateA());
    }

    private class Validate implements IBackValidate {

        @Override
        public void invalid(InvalidateFormContainer errMess) {
            publish(dType, DataActionEnum.ChangeViewFormToInvalidAction,
                    errMess);
        }

        @Override
        public void valid() {
            publish(dType, DataActionEnum.ValidSignal);
        }

    }

    private class ValidateA implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetViewComposeModelEdited);
            List<IVField> listMFie = FormUtil.getVList(fContainer);
            List<InvalidateMess> errMess = ValidateUtil.checkEmpty(pData,
                    listMFie);
            if (errMess != null) {
                publish(dType, DataActionEnum.ChangeViewFormToInvalidAction,
                        new InvalidateFormContainer(errMess));
                return;
            }
            CustomLoginData lData = (CustomLoginData) pData;
            LoginValidator.validateS(lData, user, new Validate());
        }
    }

    @Override
    public void startPublish(CellId cellId) {
    }

}