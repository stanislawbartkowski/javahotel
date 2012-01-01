/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.ValidateUtil;
import com.javahotel.client.abstractto.InvoicePVData;
import com.javahotel.common.command.PaymentMethod;

/**
 * @author hotel
 * 
 */
public class InvoiceValidate extends AbstractSlotContainer implements
        IDataValidateAction {

    private boolean checkError(IVModelData pa, List<IVField> eList) {
        List<InvalidateMess> errMess = ValidateUtil.checkEmpty(pa, eList);
        if (errMess != null) {
            publish(dType, DataActionEnum.InvalidSignal,
                    new InvalidateFormContainer(errMess));
            return true;
        }
        return false;

    }

    private class ValidateA implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetViewComposeModelEdited);
            InvoicePVData pa = (InvoicePVData) pData;
            List<IVField> eList = new ArrayList<IVField>();
            eList.add(MakeOutInvoiceControler.vInvoiceDate);
            if (checkError(pa, eList)) {
                return;
            }

            PaymentMethod pM = SlU.getVWidgetValue(dType, InvoiceValidate.this,
                    MakeOutInvoiceControler.vPayMethod);
            if (pM == PaymentMethod.Transfer) {
                eList = new ArrayList<IVField>();
                eList.add(MakeOutInvoiceControler.vNumbOfDays);
                eList.add(MakeOutInvoiceControler.vTermPay);
                if (checkError(pa, eList)) {
                    return;
                }
            }
            if (pa.getP().getInvoiceD().getdLines().isEmpty()) {
                List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
                InvalidateMess e = new InvalidateMess(
                        MakeOutInvoiceControler.vInvoiceDate,
                        "Brak danych do faktury !");
                errMess.add(e);
                publish(dType, DataActionEnum.InvalidSignal,
                        new InvalidateFormContainer(errMess));
                return;
            }
            publish(dType, DataActionEnum.ValidSignal);
        }

    }

    public InvoiceValidate(IDataType dType) {
        this.dType = dType;
        registerSubscriber(dType, DataActionEnum.ValidateAction,
                new ValidateA());
    }

}
