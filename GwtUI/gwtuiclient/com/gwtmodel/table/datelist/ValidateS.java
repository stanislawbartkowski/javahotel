/*
 *  Copyright 2013 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.datelist;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.validate.ValidateUtil;

/**
 * 
 * @author hotel
 */
class ValidateS extends AbstractSlotContainer implements IDataValidateAction {

    private final IDataType stringType;

    private class ValidateA implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(stringType,
                    GetActionEnum.GetViewComposeModelEdited);
            List<IVField> listMFie = new ArrayList<IVField>();
            listMFie.add(new DatePeriodField(DatePeriodField.F.DATEFROM));
            List<InvalidateMess> errMess = ValidateUtil.checkEmpty(pData,
                    listMFie);
            if (errMess != null) {
                publish(stringType, DataActionEnum.InvalidSignal,
                        new InvalidateFormContainer(errMess));
                return;
            }
            publish(stringType, DataActionEnum.ValidSignal);
        }
    }

    ValidateS(IDataType stringType) {
        this.stringType = stringType;
        registerSubscriber(stringType, DataActionEnum.ValidateAction,
                new ValidateA());

    }

    @Override
    public void startPublish(CellId cellId) {
    }
}