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
package com.gwthotel.hotel.jpa.reservation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.IHotelObjectGenSymFactory;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.hotel.jpa.entities.EHotelReservationDetail;
import com.gwthotel.hotel.jpa.entities.EHotelRoom;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservation.ReservationDetail;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.CUtil;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.shared.MUtil;

class HotelReservations extends
        AbstractJpaCrud<ReservationForm, EHotelReservation> implements
        IReservationForm {

    HotelReservations(ITransactionContextFactory eFactory,
            IHotelObjectGenSymFactory iGen) {
        super(new String[] { "findAllReservations", "findOneReservation" },
                eFactory, HotelObjects.RESERVATION, iGen);
    }

    @Override
    protected ReservationForm toT(EHotelReservation sou, EntityManager em,
            HotelId hotel) {
        ReservationForm ho = new ReservationForm();
        ho.setCustomerName(sou.getCustomer().getName());
        ho.setStatus(sou.getStatus());
        for (EHotelReservationDetail r : sou.getResDetails()) {
            ReservationDetail det = new ReservationDetail();
            det.setNoP(r.getNoP());
            det.setPrice(r.getPrice());
            det.setResDate(r.getResDate());
            if (r.getRoom() != null)
                det.setAttr(IHotelConsts.RESDETROOMNAMEPROP, r.getRoom()
                        .getName());
            if (r.getService() != null)
                det.setAttr(IHotelConsts.RESDETSERVICENAMEPROP, r.getService()
                        .getName());
            ho.getResDetail().add(det);
        }
        return ho;
    }

    @Override
    protected EHotelReservation constructE(EntityManager em, HotelId hotel) {
        return new EHotelReservation();
    }

    @Override
    protected void toE(EHotelReservation dest, ReservationForm sou,
            EntityManager em, HotelId hotel) {
        String custName = sou.getCustomerName();
        EHotelCustomer cust = JUtils.getElemE(em, hotel, "findOneCustomer",
                custName);
        dest.setCustomer(cust);
        dest.setStatus(sou.getStatus());
        List<EHotelReservationDetail> lDetails = new ArrayList<EHotelReservationDetail>();
        for (ReservationDetail r : sou.getResDetail()) {
            EHotelReservationDetail d = new EHotelReservationDetail();
            String roomName = r.getRoom();
            if (!CUtil.EmptyS(roomName)) {
                EHotelRoom room = JUtils.getElemE(em, hotel, "findOneRoom",
                        roomName);
                d.setRoom(room);
            }
            String serviceName = r.getService();
            if (!CUtil.EmptyS(serviceName)) {
                EHotelServices serv = JUtils.getElemE(em, hotel,
                        "findOneService", serviceName);
                d.setService(serv);
            }
            d.setNoP(r.getNoP());
            d.setPrice(r.getPrice());
            d.setResDate(r.getResDate());
            d.setReservation(dest);
            lDetails.add(d);
        }
        dest.setResDetails(lDetails);
    }

    @Override
    protected void beforedeleteAll(EntityManager em, HotelId hotel) {
        Query q = em.createNamedQuery("deleteAllReservationsDetails");
        q.setParameter(1, hotel.getId());
        q.executeUpdate();

    }

    @Override
    protected void beforedeleteElem(EntityManager em, HotelId hotel,
            EHotelReservation elem) {
        Query q = em
                .createNamedQuery("deleteAllReservationsDetailsForReservation");
        q.setParameter(1, elem);
        q.executeUpdate();

    }

}
