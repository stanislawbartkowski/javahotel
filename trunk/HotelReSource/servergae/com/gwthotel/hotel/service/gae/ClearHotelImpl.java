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

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.service.gae.entities.EBillPayment;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.gwthotel.hotel.service.gae.entities.EHotelCustomer;
import com.gwthotel.hotel.service.gae.entities.EHotelGuest;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceElem;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceList;
import com.gwthotel.hotel.service.gae.entities.EHotelReservation;
import com.gwthotel.hotel.service.gae.entities.EHotelRoom;
import com.gwthotel.hotel.service.gae.entities.EHotelRoomServices;
import com.gwthotel.hotel.service.gae.entities.EHotelServices;
import com.gwthotel.hotel.service.gae.entities.EResDetails;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;
import com.jythonui.server.getmess.IGetLogMess;

public class ClearHotelImpl implements IClearHotel {

    @Override
    public void clearObjects(OObjectId hotel) {
        final EObject eh = EntUtil.findEOObject(hotel);
        ofy().transact(new VoidWork() {
            public void vrun() {
                // partial list
                Class[] e = { EResDetails.class, EBillPayment.class,
                        ECustomerBill.class, EHotelGuest.class,
                        EHotelReservation.class, EHotelServices.class,
                        EHotelRoomServices.class, EHotelPriceElem.class,
                        EHotelPriceList.class, EHotelRoom.class,
                        EHotelCustomer.class };
                for (Class c : e) {
                    ofy().delete().entities(
                            (ofy().load().type(c).ancestor(eh).list()));
                }
            }
        });

    }

    @Override
    public void setTestDataToday(Date d) {
        DateFormatUtil.setTestToday(d);
    }

    @SuppressWarnings("rawtypes")
    private Class getClass(HotelObjects o) {
        switch (o) {
        case ROOM:
            return EHotelRoom.class;
        case PAYMENTS:
            return EBillPayment.class;
        case BILL:
            return ECustomerBill.class;
        case CUSTOMER:
            return EHotelCustomer.class;
        case GUESTS:
            return EHotelGuest.class;
        case PRICELIST:
            return EHotelPriceList.class;
        case RESERVATION:
            return EHotelReservation.class;
        case RESERVATIONDETAILS:
            return EResDetails.class;
        case SERVICE:
            return EHotelServices.class;
        default:
            break;
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public long numberOf(OObjectId hotel, HotelObjects o) {
        Class cl = getClass(o);
        boolean nof = true;
        if (nof) {
            List li = ofy().load().type(cl)
                    .ancestor(EntUtil.findEOObject(hotel)).list();
            return li.size();
        }
        return 0;
    }

}
