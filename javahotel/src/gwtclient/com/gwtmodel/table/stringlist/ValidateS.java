/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.stringlist;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.Empty;
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
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.ValidateUtil;

class ValidateS extends AbstractSlotContainer implements IDataValidateAction {

    private final IDataType stringType;

    private class ValidateA implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(
                    GetActionEnum.GetViewComposeModelEdited, stringType);
            List<IVField> listMFie = new ArrayList<IVField>();
            listMFie.add(Empty.getFieldType());
            List<InvalidateMess> errMess = ValidateUtil.checkEmpty(pData,
                    listMFie);
            if (errMess != null) {
                publish(DataActionEnum.InvalidSignal, stringType,
                        new InvalidateFormContainer(errMess));
                return;
            }
            publish(DataActionEnum.ValidSignal, stringType);
        }
    }

    ValidateS(IDataType stringType) {
        this.stringType = stringType;
        registerSubscriber(DataActionEnum.ValidateAction, stringType,
                new ValidateA());

    }

    public void startPublish(CellId cellId) {
    }

}
