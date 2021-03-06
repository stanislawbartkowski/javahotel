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
package com.gwthotel.hotel.objectfactoryimpl;

import javax.inject.Inject;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IGetAutomPatterns;
import com.gwthotel.hotel.IHotelObjectsFactory;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.mailing.HotelMailElem;
import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.ISharedConsts;
import com.jythonui.shared.PropDescription;

public class HotelObjectsFactory implements IHotelObjectsFactory {

    private final IGetAutomPatterns iPatt;

    @Inject
    public HotelObjectsFactory(IGetAutomPatterns iPatt) {
        this.iPatt = iPatt;
    }

    @Override
    public PropDescription construct(OObjectId hotel, HotelObjects o) {
        PropDescription outo = null;
        switch (o) {
        case RESERVATION:
            outo = new ReservationForm();
            break;
        case CUSTOMER:
            outo = new HotelCustomer();
            break;
        case PRICELIST:
            outo = new HotelPriceList();
            break;
        case ROOM:
            outo = new HotelRoom();
            break;
        case SERVICE:
            outo = new HotelServices();
            break;
        case BILL:
            outo = new CustomerBill();
            break;
        case HOTELMAIL:
            outo = new HotelMailElem();
            break;
        }
        if (outo == null)
            return null;
        String patt = iPatt.getPatt(hotel, o);
        if (patt != null)
            outo.setAttr(ISharedConsts.PATTPROP, patt);
        return outo;
    }

}
