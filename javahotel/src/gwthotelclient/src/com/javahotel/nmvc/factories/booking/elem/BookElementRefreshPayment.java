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
package com.javahotel.nmvc.factories.booking.elem;

import java.math.BigDecimal;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.PUtil;
import com.javahotel.client.abstractto.BookPaymentField;
import com.javahotel.client.calculateprice.IPaymentData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.VField;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.PaymentRowP;

/**
 * @author hotel
 * 
 */
class BookElementRefreshPayment {

    private final IPaymentData iPayment;
    private final ISlotable iSlo;
    private final IDataType dType;
    private final ICallContext iContext;
    private final ISlotable mainSlo;
    private final IDataType bookType = new DataType(DictType.BookingList);
    private final PayElemInfo initP;
    
    private PayElemInfo pInfo;

    private void setBigDecimal(IField fie, BigDecimal b) {
        DataUtil.setBigDecimal(iSlo, dType, fie, b);
    }

    private void setPrice(BigDecimal offerPrice, BigDecimal custPrice) {
        setBigDecimal(BookPaymentField.offerPrice, offerPrice);
        setBigDecimal(BookPaymentField.customerPrice, custPrice);
    }
    
    List<PaymentRowP> getPList() {
        return pInfo.getpList();
    }

    private class setPayment implements IPaymentData.ISetPaymentRows {

        @Override
        public void setRow(List<PaymentRowP> col) {
            PUtil.SumP sumP = PUtil.getPrice(col);
            setPrice(sumP.sumOffer, sumP.sumCustomer);
            pInfo.setpList(col);
        }

    }

    private class ChangeValue implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            pInfo = new PayElemInfo(dType, bookType, iSlo, mainSlo,
                    iContext);
            pInfo.setFromW();
            if (pInfo.isNotDefined()) {
                setPrice(null, null);
                return;
            }
            iPayment.setPaymentData(pInfo.getSeason(), pInfo.getSprice(),
                    pInfo.getdFrom(), pInfo.getdTo(), pInfo.getService(),
                    new setPayment());
        }

    }

    BookElementRefreshPayment(IDataType dType, ISlotable iSlo,
            ICallContext iContext, ISlotable mainSlo) {
        this.iContext = iContext;
        this.mainSlo = mainSlo;
        iPayment = HInjector.getI().getPaymentData();
        this.iSlo = iSlo;
        this.dType = dType;
        this.initP = new PayElemInfo(dType, bookType, iSlo, mainSlo, iContext);
        iSlo.getSlContainer().registerSubscriber(dType,
                new VField(BookElemP.F.checkIn), new ChangeValue());
        iSlo.getSlContainer().registerSubscriber(dType,
                new VField(BookElemP.F.checkOut), new ChangeValue());
        iSlo.getSlContainer().registerSubscriber(dType,
                new VField(BookElemP.F.service), new ChangeValue());
        initP.initW();
    }

}
