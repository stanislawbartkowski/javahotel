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
package com.gwthotel.hotel.service.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
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
import com.jythonui.server.getmess.IGetLogMess;

public class ClearHotelImpl implements IClearHotel {

    private final IGetLogMess lMess;

    @Inject
    public ClearHotelImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    @Override
    public void clearObjects(HotelId hotel) {
        final EHotel eh = DictUtil.findEHotel(lMess, hotel);
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

}
