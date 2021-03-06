/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.prices.HotelPriceElem;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceElem;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;

public class HotelPriceElemImpl implements IHotelPriceElem {

    static {
        ObjectifyService.register(EHotelPriceElem.class);
    }

    private EObject findEHotel(OObjectId hotel) {
        return EntUtil.findEOObject(hotel);
    }

    @Override
    public List<HotelPriceElem> getPricesForPriceList(OObjectId hotel,
            String pricelist) {
        EObject ho = findEHotel(hotel);
        List<EHotelPriceElem> li = ofy().load().type(EHotelPriceElem.class)
                .ancestor(ho).filter("pricelistName == ", pricelist).list();
        List<HotelPriceElem> outList = new ArrayList<HotelPriceElem>();
        for (EHotelPriceElem e : li) {
            HotelPriceElem elem = new HotelPriceElem();
            // elem.setWeekendPrice(e.getWeekendPrice());
            // elem.setWorkingPrice(e.getWorkingPrice());
            elem.setPrice(DictUtil.toBD(e.getPrice()));
            elem.setChildrenPrice(DictUtil.toBD(e.getChildrenPrice()));
            elem.setExtrabedsPrice(DictUtil.toBD(e.getExtrabedsPrice()));
            elem.setPriceList(pricelist);
            elem.setService(e.getServiceName());
            outList.add(elem);
        }
        return outList;
    }

    @Override
    public void savePricesForPriceList(final OObjectId hotel,
            final String pricelist, final List<HotelPriceElem> pList) {
        final EObject ho = findEHotel(hotel);
        final List<EHotelPriceElem> li = ofy().load()
                .type(EHotelPriceElem.class).ancestor(ho)
                .filter("pricelistName == ", pricelist).list();
        ofy().transact(new VoidWork() {

            @Override
            public void vrun() {
                ofy().delete().entities(li).now();
                for (HotelPriceElem e : pList) {
                    EHotelPriceElem elem = new EHotelPriceElem();
                    // elem.setWeekendPrice(e.getWeekendPrice());
                    // elem.setWorkingPrice(e.getWorkingPrice());
                    elem.setPrice(DictUtil.toDouble(e.getPrice(), false));
                    elem.setChildrenPrice(DictUtil.toDouble(
                            e.getChildrenPrice(), true));
                    elem.setExtrabedsPrice(DictUtil.toDouble(
                            e.getExtrabedsPrice(), true));
                    elem.setHotel(ho);
                    elem.setPricelistName(pricelist);
                    elem.setServiceName(e.getService());
                    ofy().save().entity(elem).now();
                }
            }
        });
    }

    @Override
    public void deleteAll(final OObjectId hotel) {
        final EObject ho = findEHotel(hotel);
        ofy().transact(new VoidWork() {

            @Override
            public void vrun() {
                List<EHotelPriceElem> li = ofy().load()
                        .type(EHotelPriceElem.class).ancestor(ho).list();
                ofy().delete().entities(li);
            }
        });
    }

}
