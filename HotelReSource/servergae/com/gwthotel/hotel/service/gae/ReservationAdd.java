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

import java.util.List;

import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.service.gae.entities.EHotelReservation;
import com.gwthotel.hotel.service.gae.entities.EResDetails;
import com.gwtmodel.table.common.CUtil;
import com.jython.serversecurity.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;
import com.jythonui.server.BUtil;
import com.jythonui.server.getmess.IGetLogMess;

class ReservationAdd {

    private final ReservationForm elem;
    private final EObject ho;
    private final OObjectId hotel;
    private final boolean change;
    private List<EResDetails> deList;
    private final IHotelObjectGenSym iGen;

    ReservationAdd(OObjectId hotel, IGetLogMess lMess, ReservationForm elem,
            boolean change, IHotelObjectGenSym iGen) {
        this.elem = elem;
        this.hotel = hotel;
        ho = DictUtil.findEHotel(lMess, hotel);
        this.change = change;
        this.iGen = iGen;
    }

    void beforeAdd() {
        for (ReservationPaymentDetail d : elem.getResDetail()) {
            if (CUtil.EmptyS(d.getGuestName()))
                d.setGuestName(elem.getCustomerName());
            d.setServiceType(ServiceType.HOTEL);
        }
        if (change)
            deList = DictUtil.findResDetailsForRes(ho, elem.getName(),
                    ServiceType.HOTEL);
        else
            iGen.genSym(hotel, elem, HotelObjects.RESERVATION);

    }

    void addTran() {
        final EHotelReservation e;
        if (change)
            e = DictUtil.findReservation(ho, elem.getName());
        else
            e = new EHotelReservation();
        BUtil.setCreateModif(hotel.getUserName(), e, !change);
        EntUtil.toEDict(e, elem);
        e.setCustomer(DictUtil.findCustomer(ho, elem.getCustomerName()));
        e.setStatus(elem.getStatus());
        final List<EResDetails> eRes = DictUtil.toED(ho, null,
                elem.getResDetail());
        ofy().transact(new VoidWork() {
            public void vrun() {
                if (change)
                    ofy().delete().entities(deList);
                else
                    e.setHotel(ho);
                ofy().save().entity(e).now();
                for (EResDetails r : eRes) {
                    r.setReservation(e);
                    r.setHotel(ho);
                    // for hotel services copy Price to PriceTotal
                    if (r.getPriceTotal() == null)
                        r.setPriceTotal(r.getPrice());
                }
                ofy().save().entities(eRes);
            }
        });
    }

}
