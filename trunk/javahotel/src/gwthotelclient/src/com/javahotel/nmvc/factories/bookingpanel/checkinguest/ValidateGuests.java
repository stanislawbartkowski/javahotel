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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.util.FormUtil;
import com.javahotel.common.toobject.BookingP;

/**
 * @author hotel
 * 
 */

class ValidateGuests extends AbstractSlotContainer {

    private final IDataType dType;
    private final BookingP p;
    private final BoxActionMenuOptions bOptions;

    ValidateGuests(IDataType dType, BookingP p, BoxActionMenuOptions bOptions) {
        this.dType = dType;
        this.p = p;
        this.bOptions = bOptions;
        getSlContainer().registerSubscriber(dType,
                DataActionEnum.ValidateAction, new ValidateSignal());
    }

    private class ValidateSignal implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {

            IDataListType dList = SlU.getIDataListType(dType,
                    ValidateGuests.this);

            // put data from view to list
            int li = 0;
            List<IGetSetVField> vList;
            for (IVModelData m : dList.getList()) {
                vList = SlU.getVListFromEditTable(dType, ValidateGuests.this,
                        li);
                li++;
                FormUtil.copyFromViewToModel(vList, m);
            }

            // now count number of guest check-ined
            UtilCust.ECountParam eC = UtilCust.countGuests(dList);
            if (!eC.wasEdited || (eC.guestNo == 0)) {
                getSlContainer().publish(
                        bOptions.constructRemoveFormDialogSlotType());
                return;
            }
            int noP = p.getNoPersons();
            String ask = null;
            if (noP == eC.guestNo) {
                ask = "Zapisać zameldowanych gości ?";
            } else if (noP > eC.guestNo) {
                ask = "Liczba zameldowanych gości (" + eC.guestNo
                        + ") jest mniejsza niż liczba rezerwacji (" + noP
                        + "). Czy zapisać jak jest ?";
            } else {
                ask = "Liczba zameldowanych gości (" + eC.guestNo
                        + ") jest większa niż liczba rezerwacji (" + noP
                        + "). Czy zapisać jak jest ? ";
            }
            SlU.publishValidWithAsk(dType, ValidateGuests.this, slContext, ask);
        }
    }

}
