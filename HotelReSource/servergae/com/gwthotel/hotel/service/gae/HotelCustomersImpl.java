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

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.service.gae.crud.CrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelCustomer;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelCustomersImpl extends
        CrudGaeAbstract<HotelCustomer, EHotelCustomer> implements
        IHotelCustomers {

    static {
        ObjectifyService.register(EHotelCustomer.class);
    }

    @Inject
    public HotelCustomersImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess,
            IHotelObjectGenSym iGen) {
        super(lMess, EHotelCustomer.class, HotelObjects.CUSTOMER, iGen);
    }

    @Override
    protected HotelCustomer constructProp(EHotel ho, EHotelCustomer e) {
        HotelCustomer cu = new HotelCustomer();
        HUtils.toTProperties(HUtils.getCustomerFields(), cu, e);
        return cu;
    }

    @Override
    protected EHotelCustomer constructE() {
        return new EHotelCustomer();
    }

    @Override
    protected void toE(EHotel ho, EHotelCustomer e, HotelCustomer t) {
        HUtils.toEProperties(HUtils.getCustomerFields(), e, t);

    }

    @Override
    protected void beforeDelete(DeleteItem i, EHotel ho, EHotelCustomer elem) {
    }

}
