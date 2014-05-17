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
package com.gwthotel.hotel.jpa.customers;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.IHotelObjectGenSymFactory;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.jython.serversecurity.OObjectId;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;

public class HotelJpaCustomers extends AbstractJpaCrud<HotelCustomer, EHotelCustomer>
        implements IHotelCustomers {

    @Inject
    public HotelJpaCustomers(ITransactionContextFactory eFactory,
            IHotelObjectGenSymFactory iGen) {
        super(new String[] { "findAllCustomers", "findOneCustomer" }, eFactory,
                HotelObjects.CUSTOMER, iGen, EHotelCustomer.class);
    }

    @Override
    protected HotelCustomer toT(EHotelCustomer sou, EntityManager em,
            OObjectId hotel) {
        HotelCustomer ho = new HotelCustomer();
        toTProperties(HUtils.getCustomerFields(), ho, sou);
        ho.setSex(sou.getSex());
        ho.setDoctype(sou.getDoctype());
        return ho;
    }

    @Override
    protected EHotelCustomer constructE(EntityManager em, OObjectId hotel) {
        return new EHotelCustomer();
    }

    @Override
    protected void toE(EHotelCustomer dest, HotelCustomer sou,
            EntityManager em, OObjectId hotel) {
        toEProperties(HUtils.getCustomerFields(), dest, sou);
        if ((sou.getDoctype() == 0) || (sou.getSex() == 0)) {
            String mess = lMess.getMess(IHError.HERROR022,
                    IHMess.CUSTOMERSEXDOCNULL);
            errorLog(mess);
        }
        dest.setDoctype(sou.getDoctype());
        dest.setSex(sou.getSex());
    }

    @Override
    protected void beforedeleteElem(EntityManager em, OObjectId hotel,
            EHotelCustomer elem) {
        String[] lQuery = { "deleteGuestForCustomer",
                "deleteAllReservationDetailsForCustomer" };
        JUtils.runQueryForObject(em, elem, lQuery);
    }

    @Override
    protected void afterAddChange(EntityManager em, OObjectId hotel,
            HotelCustomer prop, EHotelCustomer elem, boolean add) {
    }

}
