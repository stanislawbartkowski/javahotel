/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.ValidateUtil;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataUtil;
import com.javahotel.nmvc.common.VField;

public class ValidateAction extends AbstractSlotContainer implements
        IDataValidateAction {

    private final DictValidatorFactory valFactory;

    private class Validate implements ISignalValidate {

        @Override
        public void failue(IErrorMessage errmess) {
            InvalidateFormContainer eContainer = DataUtil.convert(errmess);
            publishValidSignal(eContainer);
        }

        @Override
        public void success() {
            publishValidSignal(null);
        }
    }

    private void publishValidSignal(InvalidateFormContainer errContainer) {
        if (errContainer == null) {
            publish(DataActionEnum.ValidSignal, dType);
        } else {
            publish(DataActionEnum.InvalidSignal, dType, errContainer);
        }
    }

    private void validateE(PersistTypeEnum persistTypeEnum, IVModelData mData,
            FormLineContainer fContainer) {
        int action = DataUtil.vTypetoAction(persistTypeEnum);
        List<IVField> listMFie = DataUtil.constructEmptyList(dType, action);
        Set<IVField> ignoreV = new HashSet<IVField>();

        DataType da = (DataType) dType;
        if (da.getdType() == DictType.CustomerList) {
            for (FormField f : fContainer.getfList()) {
                IFormLineView i = f.getELine();
                VField v = (VField) f.getFie();
                if (v.getFie() == DictionaryP.F.name) {
                    if (i.getChooseResult() == IFormLineView.CHOOSECHECKTRUE) {
                        for (IVField vv : listMFie) {
                            if (vv.eq(v)) {
                                ignoreV.add(vv);
                            }
                        }
                    }
                }
            }
        }
        List<InvalidateMess> errMess = ValidateUtil.checkEmpty(mData, listMFie,
                ignoreV);
        if (errMess == null) {
            if (da.isAllPersons()) {
                LoginData lo = (LoginData) mData;
                String password = lo.getPassword();
                String repassword = (String) lo.getF(new LoginField(
                        LoginField.F.REPASSWORD));
                if (!CUtil.EmptyS(password)) {
                    if (!CUtil.EqNS(password, repassword)) {
                        errMess = new ArrayList<InvalidateMess>();
                        errMess.add(new InvalidateMess(new LoginField(
                                LoginField.F.PASSWORD),
                                "Hasła się nie zgadzają"));
                        publishValidSignal(new InvalidateFormContainer(errMess));
                        return;
                    }
                }
                publishValidSignal(null);
                return;
            }
            IRecordValidator val = valFactory.getValidator(new DictData(da.getdType()), true);
            RecordModel mo = DataUtil.toRecordModel(mData);
            val.validateS(action, mo, new Validate());
            return;
        }
        publishValidSignal(new InvalidateFormContainer(errMess));
    }

    private class ValidateA implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(
                    GetActionEnum.GetViewComposeModelEdited, dType);
            FormLineContainer fContainer = getGetterContainer(dType);
            validateE(slContext.getPersistType(), pData, fContainer);
        }
    }

    public ValidateAction(DictValidatorFactory valFactory, IDataType dType) {
        this.valFactory = valFactory;
        this.dType = dType;
        registerSubscriber(DataActionEnum.ValidateAction, dType,
                new ValidateA());
    }
}
