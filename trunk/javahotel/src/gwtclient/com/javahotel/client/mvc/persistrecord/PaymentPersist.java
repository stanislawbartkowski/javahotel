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
package com.javahotel.client.mvc.persistrecord;

import com.javahotel.client.mvc.auxabstract.PaymentStateModel;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictdata.model.IPaymentModel;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.client.IResLocator;
import com.javahotel.client.CallBackHotel;
import com.javahotel.client.GWTGetService;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PaymentPersist implements IPersistRecord {

    private final IResLocator rI;

    PaymentPersist(IResLocator rI) {
        this.rI = rI;
    }

    private class CallB extends CallBackHotel {

        private final IPersistResult ires;

        CallB(final IPersistResult iRes) {
            super(rI);
            this.ires = iRes;
        }

        @Override
        public void onMySuccess(final Object arg) {
            CallSuccess.callI(ires, 0, null, arg);
        }
    }

    public void persist(final int action,
            final RecordModel a, final IPersistResult ires) {
        IPaymentModel pM = (IPaymentModel) a.getAuxData1();
        PaymentStateModel pMo = (PaymentStateModel) a.getA();
        PaymentP pe = pMo.getO1();
        pe.setSumOp(true);
        BookingStateP bState = pMo.getO2();

        CommandParam pa = rI.getR().getHotelCommandParam();
        pa.setDownPayment(pe);
        pa.setStateP(bState);
        pa.setReservName(pM.getResName());
        GWTGetService.getService().hotelOp(
                HotelOpType.payDownPaymentStateNoChange, pa,
                new CallB(ires));
    }
}
