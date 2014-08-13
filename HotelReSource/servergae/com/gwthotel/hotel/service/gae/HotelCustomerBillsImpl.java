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

import java.math.BigInteger;

import javax.inject.Inject;

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.service.gae.crud.HotelCrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.crud.ICrudObjectGenSym;

public class HotelCustomerBillsImpl extends
        HotelCrudGaeAbstract<CustomerBill, ECustomerBill> implements
        ICustomerBills {

    static {
        ObjectifyService.register(ECustomerBill.class);
    }

    @Inject
    public HotelCustomerBillsImpl(ICrudObjectGenSym iGen) {
        super(ECustomerBill.class, HotelObjects.BILL, iGen);
    }

    @Override
    protected CustomerBill constructProp(EObject ho, ECustomerBill e) {
        return DictUtil.toCustomerBill(e);
    }

    @Override
    protected ECustomerBill constructE() {
        return new ECustomerBill();
    }

    @Override
    protected void toE(EObject ho, ECustomerBill e, CustomerBill t) {
        e.setDateOfPayment(t.getDateOfPayment());
        e.setIssueDate(t.getIssueDate());
        e.setPayer(DictUtil.findCustomer(ho, t.getPayer()));
        e.setReservation(DictUtil.findReservation(ho, t.getReseName()));
        e.getResDetails().clear();
        // important: after calling this method from Jython there could be
        // BigInteger instead of Long
        for (Object o : t.getPayList()) {
            Long l;
            if (o instanceof BigInteger)
                l = ((BigInteger) o).longValue();
            else
                l = (Long) o;
            e.getResDetails().add(l);
        }
    }

    @Override
    protected void beforeDelete(DeleteItem i, EObject ho, ECustomerBill elem) {
    }

}
