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
package com.gwthotel.hotel.jpa.pricelist;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.IHotelObjectGenSymFactory;
import com.gwthotel.hotel.jpa.entities.EHotelPriceList;
import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.shared.MUtil;

class HotelJpaPriceList extends
        AbstractJpaCrud<HotelPriceList, EHotelPriceList> implements
        IHotelPriceList {

    HotelJpaPriceList(ITransactionContextFactory eFactory,
            IHotelObjectGenSymFactory iGen) {
        super(new String[] { "findAllPriceLists", "findOnePriceList" },
                eFactory, HotelObjects.PRICELIST, iGen);
    }

    @Override
    protected HotelPriceList toT(EHotelPriceList sou, EntityManager em,
            HotelId hotel) {
        HotelPriceList ho = new HotelPriceList();
        ho.setFromDate(sou.getPriceFrom());
        ho.setToDate(sou.getPriceTo());
        return ho;
    }

    @Override
    protected EHotelPriceList constructE(EntityManager em, HotelId hotel) {
        return new EHotelPriceList();
    }

    @Override
    protected void toE(EHotelPriceList dest, HotelPriceList sou,
            EntityManager em, HotelId hotel) {
        dest.setPriceFrom(sou.getFromDate());
        dest.setPriceTo(sou.getToDate());
    }

    @Override
    protected void beforedeleteAll(EntityManager em, HotelId hotel) {
        Query q = em.createNamedQuery("deletePricesForHotel");
        q.setParameter(1, hotel.getId());
        q.executeUpdate();
    }

    @Override
    protected void beforedeleteElem(EntityManager em, HotelId hotel,
            EHotelPriceList elem) {
        Query q = em.createNamedQuery("deletePricesForHotelAndPriceList");
        q.setParameter(1, hotel.getId());
        q.setParameter(2, elem);
        q.executeUpdate();
    }

}
