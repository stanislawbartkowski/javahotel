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
package com.gwthotel.hotel.jpa.bill;

import javax.persistence.EntityManager;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.IHotelObjectGenSymFactory;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.ECustomerBill;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;

class CustomerBillJpa extends AbstractJpaCrud<CustomerBill, ECustomerBill>
        implements ICustomerBills {

    CustomerBillJpa(ITransactionContextFactory eFactory,
            IHotelObjectGenSymFactory iGen) {
        super(new String[] { "findAllBills", "findOneBill" }, eFactory,
                HotelObjects.BILL, iGen);
    }

    @Override
    protected CustomerBill toT(ECustomerBill sou, EntityManager em,
            HotelId hotel) {
        CustomerBill dest = new CustomerBill();
        JUtils.toCustomerBill(em, hotel, dest, sou);
        return dest;
    }

    @Override
    protected ECustomerBill constructE(EntityManager em, HotelId hotel) {
        return new ECustomerBill();
    }

    @Override
    protected void toE(ECustomerBill dest, CustomerBill sou, EntityManager em,
            HotelId hotel) {
        String payname = sou.getPayer();
        EHotelCustomer cust = JUtils.findCustomer(em, hotel, payname);
        dest.setCustomer(cust);
        String resename = sou.getReseName();
        EHotelReservation res = JUtils.findReservation(em, hotel, resename);
        dest.setReservation(res);
        dest.setResDetails(sou.getPayList());
        dest.setIssueDate(sou.getIssueDate());
        dest.setDateOfPayment(sou.getIssueDate());
    }

    @Override
    protected void beforedeleteAll(EntityManager em, HotelId hotel) {
        JUtils.runQueryForHotels(em, hotel,
                new String[] { "removeAllPayments" });

    }

    @Override
    protected void beforedeleteElem(EntityManager em, HotelId hotel,
            ECustomerBill elem) {
        JUtils.runQueryForObject(em, elem,
                new String[] { "removePaymentsforBill" });
    }

    @Override
    protected void afterAddChange(EntityManager em, HotelId hotel,
            CustomerBill prop, ECustomerBill elem, boolean add) {

    }

}