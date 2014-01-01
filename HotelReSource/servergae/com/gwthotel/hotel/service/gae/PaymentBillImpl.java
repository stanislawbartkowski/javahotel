/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.service.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.payment.PaymentBill;
import com.gwthotel.hotel.service.gae.entities.EBillPayment;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class PaymentBillImpl implements IPaymentBillOp {

    static {
        ObjectifyService.register(EBillPayment.class);
    }

    private final IGetLogMess lMess;

    @Inject
    public PaymentBillImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    @Override
    public List<PaymentBill> getPaymentsForBill(HotelId hotel, String billName) {
        EHotel ho = DictUtil.findEHotel(lMess, hotel);
        List<EBillPayment> li = ofy().load().type(EBillPayment.class)
                .ancestor(ho).filter("billName == ", billName).list();
        List<PaymentBill> bList = new ArrayList<PaymentBill>();
        for (EBillPayment b : li) {
            PaymentBill bi = new PaymentBill();
            bi.setBillName(b.getCustomerBill().getName());
            bi.setDateOfPayment(b.getDateOfPayment());
            bi.setId(b.getId());
            bi.setPaymentTotal(b.getPaymentTotal());
            bi.setPaymentMethod(b.getPaymentMethod());
            bi.setDescription(b.getDescription());
            bList.add(bi);
        }
        return bList;
    }

    @Override
    public void addPaymentForBill(HotelId hotel, String billName,
            PaymentBill payment) {
        EHotel ho = DictUtil.findEHotel(lMess, hotel);
        ECustomerBill eB = DictUtil.findCustomerBill(ho, billName);
        EBillPayment pa = new EBillPayment();
        pa.setCustomerBill(eB);
        pa.setDateOfPayment(payment.getDateOfPayment());
        pa.setDescription(payment.getDescription());
        pa.setHotel(ho);
        pa.setPaymentMethod(payment.getPaymentMethod());
        pa.setPaymentTotal(payment.getPaymentTotal());
        ofy().save().entity(pa).now();
    }

    @Override
    public void removePaymentForBill(HotelId hotel, String billName,
            Long paymentId) {
        EHotel ho = DictUtil.findEHotel(lMess, hotel);
        Key<EBillPayment> bKey = Key.create(Key.create(ho), EBillPayment.class,
                paymentId);
        EBillPayment b = ofy().load().key(bKey).now();
        ofy().delete().entities(b).now();
    }

}
