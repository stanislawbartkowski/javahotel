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
import javax.persistence.EntityManagerFactory;

import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.jythonui.server.getmess.IGetLogMess;

class HotelJpaCustomers extends AbstractJpaCrud<HotelCustomer, EHotelCustomer>
        implements IHotelCustomers {

    HotelJpaCustomers(EntityManagerFactory eFactory, IGetLogMess lMess) {
        super(new String[] { "findAllCustomers", "findOneCustomer",
                "deleteAllCustomers" }, eFactory, lMess);
    }

    @Override
    protected HotelCustomer toT(EHotelCustomer sou) {
        HotelCustomer ho = new HotelCustomer();
        toTProperties(HUtils.getCustomerFields(), ho, sou);
        return ho;
    }

    @Override
    protected EHotelCustomer constructE() {
        return new EHotelCustomer();
    }

    @Override
    protected void toE(EHotelCustomer dest, HotelCustomer sou) {
        toEProperties(HUtils.getCustomerFields(), dest, sou);
    }

    @Override
    protected void beforedeleteAll(EntityManager em, String hotel) {

    }

    @Override
    protected void beforedeleteElem(EntityManager em, String hotel,
            EHotelCustomer elem) {

    }

}
