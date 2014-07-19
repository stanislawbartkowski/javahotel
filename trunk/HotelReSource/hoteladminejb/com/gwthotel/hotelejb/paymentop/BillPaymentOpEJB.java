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
package com.gwthotel.hotelejb.paymentop;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import com.google.inject.Inject;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.payment.PaymentBill;
import com.gwthotel.shared.IHotelConsts;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.defa.GuiceInterceptor;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@EJB(name = IHotelConsts.HOTELPAYMENTOPJNDI, beanInterface = IPaymentBillOp.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class BillPaymentOpEJB implements IPaymentBillOp {

    private IPaymentBillOp service;

    @Inject
    public void injectHotelServices(IPaymentBillOp injectedServices) {
        service = injectedServices;
    }

    @Override
    public List<PaymentBill> getPaymentsForBill(OObjectId hotel, String billName) {
        return service.getPaymentsForBill(hotel, billName);
    }

    @Override
    public void addPaymentForBill(OObjectId hotel, String billName,
            PaymentBill payment) {
        service.addPaymentForBill(hotel, billName, payment);

    }

    @Override
    public void removePaymentForBill(OObjectId hotel, String billName,
            Long paymentId) {
        service.removePaymentForBill(hotel, billName, paymentId);
    }

}
