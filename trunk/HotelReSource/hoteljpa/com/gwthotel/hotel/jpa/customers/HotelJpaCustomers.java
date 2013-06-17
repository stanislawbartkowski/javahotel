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
package com.gwthotel.hotel.jpa.customers;

import javax.persistence.EntityManager;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.IHotelObjectGenSymFactory;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;

class HotelJpaCustomers extends AbstractJpaCrud<HotelCustomer, EHotelCustomer>
        implements IHotelCustomers {

    HotelJpaCustomers(ITransactionContextFactory eFactory,
            IHotelObjectGenSymFactory iGen) {
        super(new String[] { "findAllCustomers", "findOneCustomer",
                "deleteAllCustomers" }, eFactory, HotelObjects.CUSTOMER, iGen);
    }

    @Override
    protected HotelCustomer toT(EHotelCustomer sou, EntityManager em,
            HotelId hotel) {
        HotelCustomer ho = new HotelCustomer();
        toTProperties(HUtils.getCustomerFields(), ho, sou);
        return ho;
    }

    @Override
    protected EHotelCustomer constructE(EntityManager em, HotelId hotel) {
        return new EHotelCustomer();
    }

    @Override
    protected void toE(EHotelCustomer dest, HotelCustomer sou,
            EntityManager em, HotelId hotel) {
        toEProperties(HUtils.getCustomerFields(), dest, sou);
    }

    @Override
    protected void beforedeleteAll(EntityManager em, HotelId hotel) {

    }

    @Override
    protected void beforedeleteElem(EntityManager em, HotelId hotel,
            EHotelCustomer elem) {

    }

}
