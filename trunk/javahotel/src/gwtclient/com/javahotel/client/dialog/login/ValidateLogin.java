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
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.util.FormUtil;

class ValidateLogin extends AbstractSlotContainer implements
        IDataValidateAction {

    private final IDataType dType;
    private final boolean user;
    private final FormLineContainer fContainer;

    ValidateLogin(IDataType dType, boolean user, FormLineContainer fContainer) {
        this.dType = dType;
        this.user = user;
        this.fContainer = fContainer;
        registerSubscriber(DataActionEnum.ValidateAction, dType,
                new ValidateA());
    }
    
    private class Validate implements IBackValidate {

        public void invalid(InvalidateFormContainer errMess) {
            publish(DataActionEnum.ChangeViewFormToInvalidAction, dType, errMess);            
        }

        public void valid() {
            publish(DataActionEnum.ValidSignal, dType);            
        }
        
    }

    private class ValidateA implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(
                    GetActionEnum.GetViewComposeModelEdited, dType);
            List<IVField> listMFie = FormUtil.getVList(fContainer);
            List<InvalidateMess> errMess = ValidateUtil.checkEmpty(pData,
                    listMFie);
            if (errMess != null) {
                publish(DataActionEnum.ChangeViewFormToInvalidAction, dType,
                        new InvalidateFormContainer(errMess));
                return;
            }
            CustomLoginData lData = (CustomLoginData) pData;
            LoginValidator.validateS(lData, user, new Validate());
        }
    }

    public void startPublish(CellId cellId) {

    }

}
