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
package com.gwthotel.hotel.jpa.prices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelPriceElem;
import com.gwthotel.hotel.jpa.entities.EHotelPriceList;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.prices.HotelPriceElem;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;

class HotelJpaPrices implements IHotelPriceElem {

    private final ITransactionContextFactory eFactory;

    protected HotelJpaPrices(ITransactionContextFactory eFactory) {
        this.eFactory = eFactory;
    }

    private abstract class doTransaction extends JpaTransaction {

        protected final HotelId hotel;

        doTransaction(HotelId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }
    }

    private class GetPrices extends doTransaction {

        private final String priceList;
        private final List<HotelPriceElem> pList = new ArrayList<HotelPriceElem>();

        GetPrices(HotelId hotel, String priceList) {
            super(hotel);
            this.priceList = priceList;
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = JUtils.createHotelQuery(em, hotel, "findPricesForPriceList");
            q.setParameter(2, priceList);
            // do not catch exception,
            @SuppressWarnings("unchecked")
            List<EHotelPriceElem> list = q.getResultList();
            for (EHotelPriceElem p : list) {
                HotelPriceElem e = new HotelPriceElem();
                e.setPriceList(p.getPricelist().getName());
                e.setService(p.getService().getName());
                e.setWeekendPrice(p.getWeekendprice());
                e.setWorkingPrice(p.getWorkingprice());
                pList.add(e);
            }
        }

    }

    @Override
    public List<HotelPriceElem> getPricesForPriceList(HotelId hotel,
            String pricelist) {
        GetPrices comm = new GetPrices(hotel, pricelist);
        comm.executeTran();
        return comm.pList;
    }

    private class SavePrices extends doTransaction {
        private final String priceList;
        private final List<HotelPriceElem> pList;

        SavePrices(HotelId hotel, String pricelist, List<HotelPriceElem> pList) {
            super(hotel);
            this.priceList = pricelist;
            this.pList = pList;
        }

        @Override
        protected void dosth(EntityManager em) {
            Map<String, EHotelServices> mService = new HashMap<String, EHotelServices>();

            Query q = JUtils.createHotelQuery(em, hotel, "findOnePriceList");
            q.setParameter(2, priceList);
            EHotelPriceList ePriceList = (EHotelPriceList) q.getSingleResult();
            // do not catch exception, one element is expected

            // remove all price elems
            q = em.createNamedQuery("deletePricesForHotelAndPriceList");
            q.setParameter(1, ePriceList);
            q.executeUpdate();

            // now analize elems
            for (HotelPriceElem e : pList) {
                String service = e.getService();
                EHotelServices eS = mService.get(service);
                if (eS == null) {
                    eS = JUtils.findService(em, hotel, service);
                }
                EHotelPriceElem eElem = new EHotelPriceElem();
                eElem.setHotel(hotel.getId());
                eElem.setPricelist(ePriceList);
                eElem.setService(eS);
                eElem.setWeekendprice(e.getWeekendPrice());
                eElem.setWorkingprice(e.getWorkingPrice());
                em.persist(eElem);
            }
        }

    }

    @Override
    public void savePricesForPriceList(HotelId hotel, String pricelist,
            List<HotelPriceElem> pList) {
        SavePrices comma = new SavePrices(hotel, pricelist, pList);
        comma.executeTran();
    }

    private class DeleteAllPrices extends doTransaction {

        DeleteAllPrices(HotelId hotel) {
            super(hotel);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = JUtils
                    .createHotelQuery(em, hotel, "deletePricesForHotel");
            q.executeUpdate();

        }

    }

    @Override
    public void deleteAll(HotelId hotel) {
        DeleteAllPrices comm = new DeleteAllPrices(hotel);
        comm.executeTran();

    }

}
