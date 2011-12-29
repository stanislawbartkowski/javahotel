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
package com.javahotel.nmvc.factories.validate;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.view.ValidateUtil;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.VField;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.ServiceDictionaryP;

class ValidateAction extends AbstractSlotContainer implements
        IDataValidateAction {

    private final EmptyColFactory eFactory = new EmptyColFactory();

    private class ValidateA implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            DataType da = (DataType) dType;
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetViewComposeModelEdited);
            FormLineContainer fContainer = getGetterContainer(dType);
            if (!ValidateEmpty.validateE(getSlContainer(), da,
                    slContext.getPersistType(), pData, fContainer,
                    eFactory.getEmptyCol(da, slContext.getPersistType()))) {
                return;
            }
            List<InvalidateMess> errMess = null;
            if (da.isDictType()) {
                switch (da.getdType()) {
                case OffSeasonDict:
                    errMess = ValidateUtil.checkDate(pData, new VField(
                            OfferSeasonP.F.startp), new VField(
                            OfferSeasonP.F.endp));
                    break;
                case ServiceDict:
                    ServiceDictionaryP se = DataUtil.getData(pData);
                    if (se.getServType().isRoomBooking()
                            && ((se.getNoPerson() == null) || se.getNoPerson()
                                    .intValue() == 0)) {
                        errMess = new ArrayList<InvalidateMess>();
                        InvalidateMess e = new InvalidateMess(new VField(
                                ServiceDictionaryP.F.noPerson),
                                "Usługa 'nocleg' wymaga podania liczny osób");
                        errMess.add(e);
                    }
                    break;
                case BookingList:
                    errMess = BookingValidate.check(pData);
                    break;
                default:
                    break;

                }
            }
            if (da.isAddType()) {
                switch (da.getAddType()) {
                case BookRoom:
                    errMess = ValidateUtil.checkDate(pData, new VField(
                            BookElemP.F.checkIn), new VField(
                            BookElemP.F.checkOut));
                    break;
                }
            }
            if (errMess != null) {
                P.publishValidSignalE(getSlContainer(), da, errMess);
                return;
            }
            PersistTypeEnum action = slContext.getPersistType();
            ValidateOnServer.validateS(getSlContainer(), da, action, pData);
        }
    }

    ValidateAction(IDataType dType) {
        this.dType = dType;
        registerSubscriber(dType, DataActionEnum.ValidateAction,
                new ValidateA());
    }
}
