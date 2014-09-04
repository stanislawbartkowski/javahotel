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
package com.gwthotel.hotel.jpa.bill;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.jpa.HotelAbstractJpaCrud;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.ECustomerBill;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;

public class CustomerBillJpa extends
        HotelAbstractJpaCrud<CustomerBill, ECustomerBill> implements
        ICustomerBills {

    @Inject
    public CustomerBillJpa(ITransactionContextFactory eFactory,
            IJpaObjectGenSymFactory iGen) {
        super(new String[] { "findAllBills", "findOneBill" }, eFactory,
                HotelObjects.BILL, iGen, ECustomerBill.class);
    }

    @Override
    protected CustomerBill toT(ECustomerBill sou, EntityManager em,
            OObjectId hotel) {
        CustomerBill dest = new CustomerBill();
        JUtils.toCustomerBill(em, hotel, dest, sou);
        return dest;
    }

    // Hibernate
    // Convert BigInteger to Long (if exists)
    // otherwise Hibernate breaks
    private List<Long> convertToLong(List<Long> list) {
        List<Long> dest = new ArrayList<Long>();
        for (Object o : list) {
            if (o instanceof Long)
                dest.add((Long) o);
            else if (o instanceof BigInteger) {
                BigInteger b = (BigInteger) o;
                dest.add(b.longValue());
            } else
                errorMess(hMess, IHError.HERROR024, IHMess.CUSTOMERBILLJPA, o
                        .getClass().getName());

        }
        return dest;
    }

    @Override
    protected void toE(ECustomerBill dest, CustomerBill sou, EntityManager em,
            OObjectId hotel) {
        String payname = sou.getPayer();
        EHotelCustomer cust = JUtils.findCustomer(em, hotel, payname);
        dest.setCustomer(cust);
        String resename = sou.getReseName();
        EHotelReservation res = JUtils.findReservation(em, hotel, resename);
        dest.setReservation(res);
        dest.setResDetails(convertToLong(sou.getPayList()));
        dest.setIssueDate(sou.getIssueDate());
        dest.setDateOfPayment(sou.getDateOfPayment());
    }

    @Override
    protected void beforedeleteElem(EntityManager em, OObjectId hotel,
            ECustomerBill elem) {
        JUtils.runQueryForObject(em, elem,
                new String[] { "removePaymentsforBill" });
    }

    @Override
    protected void afterAddChange(EntityManager em, OObjectId hotel,
            CustomerBill prop, ECustomerBill elem, boolean add) {

    }

}