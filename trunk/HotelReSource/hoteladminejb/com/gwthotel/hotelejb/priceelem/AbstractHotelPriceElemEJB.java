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
package com.gwthotel.hotelejb.priceelem;

import java.util.List;

import com.gwthotel.hotel.prices.HotelPriceElem;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.jython.serversecurity.cache.OObjectId;

abstract class AbstractHotelPriceElemEJB implements IHotelPriceElem {

    protected IHotelPriceElem service;

    @Override
    public List<HotelPriceElem> getPricesForPriceList(OObjectId hotel,
            String pricelist) {
        return service.getPricesForPriceList(hotel, pricelist);
    }

    @Override
    public void savePricesForPriceList(OObjectId hotel, String pricelist,
            List<HotelPriceElem> pList) {
        service.savePricesForPriceList(hotel, pricelist, pList);

    }

    @Override
    public void deleteAll(OObjectId hotel) {
        service.deleteAll(hotel);

    }

}
