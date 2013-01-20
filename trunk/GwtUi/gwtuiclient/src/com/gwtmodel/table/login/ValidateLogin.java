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

import com.gwtmodel.table.*;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.*;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.util.FormUtil;
import java.util.List;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public class ValidateLogin extends AbstractSlotContainer implements
        IDataValidateAction {

    public interface IValidateLogin {

        void validate(IVModelData vData, IBackValidate backValidate);
    }

    private final FormLineContainer fContainer;
    private final IValidateLogin iValidate;

    public ValidateLogin(IDataType dType, FormLineContainer fContainer,
            IValidateLogin iValidate) {
        this.dType = dType;
        this.fContainer = fContainer;
        this.iValidate = iValidate;
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
            iValidate.validate(pData, new Validate());
        }
    }

    @Override
    public void startPublish(CellId cellId) {
    }
}
