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

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.service.gae.crud.HotelCrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelCustomer;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.RUtils;
import com.jythonui.server.crud.ICrudObjectGenSym;

public class HotelCustomersImpl extends
        HotelCrudGaeAbstract<HotelCustomer, EHotelCustomer> implements
        IHotelCustomers {

    static {
        ObjectifyService.register(EHotelCustomer.class);
    }

    @Inject
    public HotelCustomersImpl(ICrudObjectGenSym iGen) {
        super(EHotelCustomer.class, HotelObjects.CUSTOMER, iGen);
    }

    @Override
    protected HotelCustomer constructProp(EObject ho, EHotelCustomer e) {
        HotelCustomer cu = new HotelCustomer();
        RUtils.toTProperties(HUtils.getCustomerFields(), cu, e);
        cu.setDoctype(e.getDoctype());
        cu.setSex(e.getSex());
        return cu;
    }

    @Override
    protected EHotelCustomer constructE() {
        return new EHotelCustomer();
    }

    @Override
    protected void toE(EObject ho, EHotelCustomer e, HotelCustomer t) {
        RUtils.toEProperties(HUtils.getCustomerFields(), e, t);
        e.setDoctype(t.getDoctype());
        e.setSex(t.getSex());
    }

    @Override
    protected void beforeDelete(DeleteItem i, EObject ho, EHotelCustomer elem) {
    }

}
