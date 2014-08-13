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

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.payment.PaymentBill;
import com.gwthotel.hotel.service.gae.entities.EBillPayment;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;

public class PaymentBillImpl implements IPaymentBillOp {

    static {
        ObjectifyService.register(EBillPayment.class);
    }

    @Override
    public List<PaymentBill> getPaymentsForBill(OObjectId hotel, String billName) {
        EObject ho = EntUtil.findEOObject(hotel);
        // List<EBillPayment> li = ofy().load().type(EBillPayment.class)
        // .ancestor(ho).filter("billName == ", billName).list();
        List<EBillPayment> li = DictUtil.findPaymentsForBill(ho, billName);
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
    public void addPaymentForBill(OObjectId hotel, String billName,
            PaymentBill payment) {
        EObject ho = EntUtil.findEOObject(hotel);
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
    public void removePaymentForBill(OObjectId hotel, String billName,
            Long paymentId) {
        EObject ho = EntUtil.findEOObject(hotel);
        Key<EBillPayment> bKey = Key.create(Key.create(ho), EBillPayment.class,
                paymentId);
        EBillPayment b = ofy().load().key(bKey).now();
        ofy().delete().entities(b).now();
    }

}
