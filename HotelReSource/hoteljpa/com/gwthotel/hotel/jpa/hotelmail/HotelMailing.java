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
package com.gwthotel.hotel.jpa.hotelmail;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.jpa.HotelAbstractJpaCrud;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelMail;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.hotel.mailing.HotelMailElem;
import com.gwthotel.hotel.mailing.IHotelMailList;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;

public class HotelMailing extends
        HotelAbstractJpaCrud<HotelMailElem, EHotelMail> implements
        IHotelMailList {

    @Inject
    public HotelMailing(ITransactionContextFactory eFactory,
            IJpaObjectGenSymFactory iGen) {
        super(new String[] { "findAllHotelMails", "findOneHotelMail" },
                eFactory, HotelObjects.HOTELMAIL, iGen, EHotelMail.class);
    }

    @Override
    protected HotelMailElem toT(EHotelMail sou, EntityManager em,
            OObjectId hotel) {
        HotelMailElem elem = new HotelMailElem();
        elem.setCustomerName(sou.getCustomer().getName());
        elem.setmType(sou.getmType());
        if (sou.getReservation() != null)
            elem.setReseName(sou.getReservation().getName());
        return elem;
    }

    @Override
    protected void toE(EHotelMail dest, HotelMailElem sou, EntityManager em,
            OObjectId hotel) {
        dest.setmType(sou.getmType());
        EHotelCustomer cust = JUtils.findCustomer(em, hotel,
                sou.getCustomerName());
        if (sou.isReseName()) {
            EHotelReservation rese = JUtils.findReservation(em, hotel,
                    sou.getReseName());
            dest.setReservation(rese);
        }
        dest.setCustomer(cust);
    }

    @Override
    protected void afterAddChange(EntityManager em, OObjectId hotel,
            HotelMailElem prop, EHotelMail elem, boolean add) {
    }

    @Override
    protected void beforedeleteElem(EntityManager em, OObjectId hotel,
            EHotelMail elem) {
    }

}
