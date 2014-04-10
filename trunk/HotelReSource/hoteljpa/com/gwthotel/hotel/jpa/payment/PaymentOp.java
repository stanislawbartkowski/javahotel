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
package com.gwthotel.hotel.jpa.payment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EBillPayment;
import com.gwthotel.hotel.jpa.entities.ECustomerBill;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.payment.PaymentBill;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jython.ui.shared.BUtil;

class PaymentOp implements IPaymentBillOp {

    private final ITransactionContextFactory eFactory;

    PaymentOp(ITransactionContextFactory eFactory) {
        this.eFactory = eFactory;
    }

    private abstract class doTransaction extends JpaTransaction {

        protected final HotelId hotel;
        protected final String billName;

        doTransaction(HotelId hotel, String billName) {
            super(eFactory);
            this.hotel = hotel;
            this.billName = billName;
        }

        ECustomerBill findBill(EntityManager em) {
            return JUtils.getElemE(em, hotel, "findOneBill", billName);
        }

        PaymentBill toB(EBillPayment e) {
            PaymentBill b = new PaymentBill();
            b.setBillName(e.getCustomerBill().getName());
            b.setDateOfPayment(e.getDateOfPayment());
            b.setDescription(e.getDescription());
            b.setPaymentTotal(e.getPaymentTotal());
            b.setPaymentMethod(e.getPaymentMethod());
            b.setId(e.getId());
            return b;
        }
    }

    private class GetPaymentsForBill extends doTransaction {

        private List<PaymentBill> pList = new ArrayList<PaymentBill>();

        GetPaymentsForBill(HotelId hotel, String billName) {
            super(hotel, billName);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = JUtils.createHotelQuery(em, hotel,
                    "findAllPaymentsForBill");
            q.setParameter(2, findBill(em));
            List<EBillPayment> rList = q.getResultList();
            for (EBillPayment e : rList)
                pList.add(toB(e));
        }

    }

    @Override
    public List<PaymentBill> getPaymentsForBill(HotelId hotel, String billName) {
        GetPaymentsForBill tran = new GetPaymentsForBill(hotel, billName);
        tran.executeTran();
        return tran.pList;
    }

    private class AddPaymentToBill extends doTransaction {

        private final PaymentBill p;

        AddPaymentToBill(HotelId hotel, String billName, PaymentBill p) {
            super(hotel, billName);
            this.p = p;
        }

        @Override
        protected void dosth(EntityManager em) {
            ECustomerBill b = findBill(em);
            EBillPayment e = new EBillPayment();
            // only creation date
            BUtil.setCreateModif(hotel.getUserName(), e, true);
            e.setCustomerBill(b);
            e.setDateOfPayment(p.getDateOfPayment());
            e.setPaymentMethod(p.getPaymentMethod());
            e.setPaymentTotal(p.getPaymentTotal());
            e.setDescription(p.getDescription());
            em.persist(e);
        }

    }

    @Override
    public void addPaymentForBill(HotelId hotel, String billName,
            PaymentBill payment) {
        AddPaymentToBill trans = new AddPaymentToBill(hotel, billName, payment);
        trans.executeTran();
    }

    private class RemovePaymentForBill extends doTransaction {

        private final Long paymentId;

        RemovePaymentForBill(HotelId hotel, String billName, Long paymentId) {
            super(hotel, billName);
            this.paymentId = paymentId;
        }

        @Override
        protected void dosth(EntityManager em) {
            // only as additional test
            findBill(em);
            EBillPayment e = em.find(EBillPayment.class, paymentId);
            em.remove(e);
        }

    }

    @Override
    public void removePaymentForBill(HotelId hotel, String billName,
            Long paymentId) {
        RemovePaymentForBill trans = new RemovePaymentForBill(hotel, billName,
                paymentId);
        trans.executeTran();
    }

}
