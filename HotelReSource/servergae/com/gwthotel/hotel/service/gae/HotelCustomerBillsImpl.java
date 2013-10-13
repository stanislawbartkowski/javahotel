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
package com.gwthotel.hotel.service.gae;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.service.gae.crud.CrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelCustomerBillsImpl extends
        CrudGaeAbstract<CustomerBill, ECustomerBill> implements ICustomerBills {

    static {
        ObjectifyService.register(ECustomerBill.class);
    }

    @Inject
    public HotelCustomerBillsImpl(
            @Named(IHotelConsts.MESSNAMED) IGetLogMess lMess,
            IHotelObjectGenSym iGen) {
        super(lMess, ECustomerBill.class, HotelObjects.BILL, iGen);
    }

    @Override
    protected CustomerBill constructProp(EHotel ho, ECustomerBill e) {
        return DictUtil.toCustomerBill(e);
    }

    @Override
    protected ECustomerBill constructE() {
        return new ECustomerBill();
    }

    @Override
    protected void toE(EHotel ho, ECustomerBill e, CustomerBill t) {
        e.setDateOfPayment(t.getDateOfPayment());
        e.setIssueDate(t.getIssueDate());
        e.setPayer(DictUtil.findCustomer(ho, t.getPayer()));
        e.setReservation(DictUtil.findReservation(ho, t.getReseName()));
        e.getResDetails().clear();
        e.getResDetails().addAll(t.getPayList());
    }

    @Override
    protected void beforeDelete(
            com.gwthotel.hotel.service.gae.crud.CrudGaeAbstract.DeleteItem i,
            EHotel ho, ECustomerBill elem) {
        // TODO Auto-generated method stub

    }

}
