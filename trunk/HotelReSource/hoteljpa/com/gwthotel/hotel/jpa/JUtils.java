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
package com.gwthotel.hotel.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.holder.HHolder;
import com.gwthotel.admin.jpa.PropUtils;
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.jpa.entities.ECustomerBill;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelDict;
import com.gwthotel.hotel.jpa.entities.EHotelPriceList;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.hotel.jpa.entities.EHotelReservationDetail;
import com.gwthotel.hotel.jpa.entities.EHotelRoom;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.shared.JythonUIFatal;

public class JUtils {

    private JUtils() {
    }

    static final private Logger log = Logger.getLogger(JUtils.class.getName());

    public static HotelServices toT(EHotelServices sou) {
        HotelServices ho = new HotelServices();
        PropUtils.copyToProp(ho, sou);
        ho.setNoPersons(sou.getNoPersons());
        ho.setAttr(IHotelConsts.VATPROP, sou.getVat());
        ho.setServiceType(sou.getServiceType());
        ho.setNoChildren(sou.getNoChildren());
        ho.setNoExtraBeds(sou.getNoExtraBeds());
        ho.setPerperson(sou.isPerperson());

        return ho;
    }

    public static void copyToEDict(HotelId hotel, EHotelDict pers,
            PropDescription elem) {
        PropUtils.copyToEDict(pers, elem);
        pers.setHotel(hotel.getId());
    }

    public static void copyE(EHotelRoom dest, HotelRoom sou) {
        dest.setNoPersons(sou.getNoPersons());
    }

    public static <E extends EHotelDict> E getElem(EntityManager em,
            HotelId hotel, String qName, String name) {
        Query q = createHotelQuery(em, hotel, qName);
        q.setParameter(2, name);
        try {
            @SuppressWarnings("unchecked")
            E pers = (E) q.getSingleResult();
            return pers;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static <E extends EHotelDict> E getElemE(EntityManager em,
            HotelId hotel, String qName, String name) {
        E e = getElem(em, hotel, qName, name);
        if (e != null)
            return e;
        String mess = HHolder.getHM().getMess(IHError.HERROR021,
                IHMess.OBJECTBYNAMECANNOTBEFOUND, name, hotel.getHotel());
        log.log(Level.SEVERE, mess, e);
        throw new JythonUIFatal(mess);
    }

    public static Query createHotelQuery(EntityManager em, HotelId hotel,
            String query) {
        Query q = em.createNamedQuery(query);
        q.setParameter(1, hotel.getId());
        return q;
    }

    public static EHotelServices findService(EntityManager em, HotelId hotel,
            String s) {
        return getElemE(em, hotel, "findOneService", s);
    }

    public static EHotelPriceList findPriceList(EntityManager em,
            HotelId hotel, String s) {
        return getElemE(em, hotel, "findOnePriceList", s);
    }

    public static EHotelCustomer findCustomer(EntityManager em, HotelId hotel,
            String s) {
        EHotelCustomer cu = getElemE(em, hotel, "findOneCustomer", s);
        return cu;
    }

    public static EHotelRoom findRoom(EntityManager em, HotelId hotel, String s) {
        EHotelRoom room = getElemE(em, hotel, "findOneRoom", s);
        return room;
    }

    public static EHotelReservation findReservation(EntityManager em,
            HotelId hotel, String s) {
        EHotelReservation res = getElemE(em, hotel, "findOneReservation", s);
        return res;
    }

    public static void runQueryForObject(EntityManager em, Object o,
            String[] remQuery) {
        for (String r : remQuery) {
            Query q = em.createNamedQuery(r);
            q.setParameter(1, o);
            q.executeUpdate();
        }
    }

    public static void runQueryForHotels(EntityManager em, HotelId hotel,
            String[] remQuery) {
        runQueryForObject(em, hotel.getId(), remQuery);
    }

    public static void ToReservationDetails(ReservationPaymentDetail det,
            EHotelReservationDetail r) {
        det.setNoP(r.getNoP());
        det.setPrice(r.getPrice());
        det.setPriceList(r.getPriceList());
        det.setNoChildren(r.getNoChildren());
        det.setPriceChildren(r.getPriceChildren());
        det.setPriceListChildren(r.getPriceListChildren());
        det.setNoExtraBeds(r.getNoExtraBeds());
        det.setPriceExtraBeds(r.getPriceExtraBeds());
        det.setPriceListExtraBeds(r.getPriceListExtraBeds());
        det.setPriceTotal(r.getTotal());
        det.setResDate(r.getResDate());
        det.setServiceType(r.getServiceType());
        det.setId(r.getId());
        det.setVat(r.getServicevat());
        det.setDescription(r.getDescription());
        det.setPerperson(r.isPerperson());

        if (r.getRoom() != null)
            det.setRoomName(r.getRoom().getName());
        if (r.getService() != null)
            det.setService(r.getService().getName());
        if (r.getCustomer() != null)
            det.setGuestName(r.getCustomer().getName());
        if (r.getPriceListName() != null)
            det.setPriceListName(r.getPriceListName().getName());
    }

    public static void ToEReservationDetails(EntityManager em, HotelId hotel,
            EHotelReservationDetail dest, ReservationPaymentDetail sou) {
        String roomName = sou.getRoomName();
        if (!CUtil.EmptyS(roomName)) {
            EHotelRoom room = findRoom(em, hotel, roomName);
            dest.setRoom(room);
        }
        String serviceName = sou.getService();
        if (!CUtil.EmptyS(serviceName)) {
            EHotelServices serv = JUtils.findService(em, hotel, serviceName);
            dest.setService(serv);
        }
        String priceListName = sou.getPriceListName();
        if (!CUtil.EmptyS(priceListName)) {
            EHotelPriceList ePrice = JUtils.findPriceList(em, hotel,
                    priceListName);
            dest.setPriceListName(ePrice);
        }
        String custName = sou.getGuestName();
        if (!CUtil.EmptyS(custName)) {
            EHotelCustomer cust = findCustomer(em, hotel, custName);
            dest.setCustomer(cust);

        }
        dest.setNoP(sou.getNoP());
        dest.setPrice(HUtils.roundB(sou.getPrice()));
        dest.setPriceList(HUtils.roundB(sou.getPriceList()));
        dest.setNoChildren(sou.getNoChildren());
        dest.setPriceChildren(HUtils.roundB(sou.getPriceChildren()));
        dest.setPriceListChildren(HUtils.roundB(sou.getPriceListChildren()));
        dest.setNoExtraBeds(sou.getNoExtraBeds());
        dest.setPriceExtraBeds(HUtils.roundB(sou.getPriceExtraBeds()));
        dest.setPriceListExtraBeds(HUtils.roundB(sou.getPriceListExtraBeds()));
        dest.setResDate(sou.getResDate());
        dest.setTotal(HUtils.roundB(sou.getPriceTotal()));
        dest.setServiceType(sou.getServiceType());
        dest.setServicevat(sou.getVat());
        dest.setDescription(sou.getDescription());
        dest.setPerperson(sou.isPerperson());
    }

    public static void toCustomerBill(EntityManager em, HotelId hotel,
            CustomerBill dest, ECustomerBill sou) {
        dest.setPayer(sou.getCustomer().getName());
        dest.setReseName(sou.getReservation().getName());
        dest.getPayList().addAll(sou.getResDetails());
        PropUtils.copyToProp(dest, sou);
        dest.setIssueDate(sou.getIssueDate());
        dest.setDateOfPayment(sou.getDateOfPayment());
        HUtils.retrieveCreateModif(dest, sou);
    }
}
