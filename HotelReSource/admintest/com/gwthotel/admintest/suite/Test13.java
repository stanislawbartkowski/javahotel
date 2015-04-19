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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;

public class Test13 extends TestHelper {

    @Test
    public void test1() {
        Date d = DateFormatUtil.toD(2013, 6, 12);
        List<ResQuery> rQuery = new ArrayList<ResQuery>();
        ResQuery q = new ResQuery();
        q.setRoomName("R10");
        q.setFromRes(d);
        q.setToRes(d);
        rQuery.add(q);
        List<ResData> resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertTrue(resList.isEmpty());

        HotelRoom ro = new HotelRoom();
        ro.setName("R10");
        ro.setNoPersons(1);
        iRooms.addElem(getH(HOTEL), ro);

        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(1);
        det.setResDate(d);
        det.setRoomName("R10");
        det.setPrice(new BigDecimal(100));
        det.setPriceTotal(new BigDecimal(100));
        det.setVat("7%");

        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        System.out.println(p.getName());
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        r.getResDetail().add(det);
        r = iRes.addElem(getH(HOTEL), r);

        resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertFalse(resList.isEmpty());

        r = iRes.findElem(getH(HOTEL), r.getName());
        assertNotNull(r);
        assertEquals("user", r.getCreationPerson());
        assertNotNull(r.getCreationDate());
        assertEquals("user", r.getModifPerson());
        assertNotNull(r.getModifDate());
        iResOp.changeStatus(getH(HOTEL), r.getName(), ResStatus.CANCEL);
        resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertTrue(resList.isEmpty());
    }

    class R {
        List<ResQuery> rQuery;
        String rese;
        Date dFrom, dTo;
    }

    private R createRese() {
        R r = new R();
        r.dFrom = DateFormatUtil.toD(2013, 6, 12);
        r.dTo = DateFormatUtil.toD(2013, 6, 15);
        r.rQuery = new ArrayList<ResQuery>();
        ResQuery q = new ResQuery();
        q.setRoomName("R10");
        q.setFromRes(r.dFrom);
        q.setToRes(r.dTo);
        r.rQuery.add(q);
        q = new ResQuery();
        q.setRoomName("R11");
        q.setFromRes(r.dFrom);
        q.setToRes(r.dTo);
        r.rQuery.add(q);
        List<ResData> resList = iResOp.queryReservation(getH(HOTEL), r.rQuery);
        assertTrue(resList.isEmpty());

        HotelRoom ro = new HotelRoom();
        ro.setName("R10");
        ro.setNoPersons(1);
        iRooms.addElem(getH(HOTEL), ro);
        ro = new HotelRoom();
        ro.setName("R11");
        ro.setNoPersons(1);
        iRooms.addElem(getH(HOTEL), ro);

        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm re = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        re.setCustomerName(p.getName());
        re.setGensymbol(true);
        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(1);
        det.setResDate(r.dFrom);
        det.setRoomName("R10");
        det.setPrice(new BigDecimal(100));
        det.setPriceTotal(new BigDecimal(100));
        det.setVat("7%");
        re.getResDetail().add(det);
        det = new ReservationPaymentDetail();
        det.setNoP(1);
        det.setResDate(r.dTo);
        det.setRoomName("R11");
        det.setPrice(new BigDecimal(100));
        det.setPriceTotal(new BigDecimal(100));
        det.setVat("7%");
        re.getResDetail().add(det);
        re = iRes.addElem(getH(HOTEL), re);
        r.rese = re.getName();
        return r;
    }

    @Test
    public void test2() {

        // ReservationForm r = null;
        R re = createRese();

        List<ResData> resList = iResOp.queryReservation(getH(HOTEL), re.rQuery);
        assertFalse(resList.isEmpty());
        assertEquals(2, resList.size());

        re.rQuery = new ArrayList<ResQuery>();
        ResQuery q = new ResQuery();
        q.setRoomName("R10");
        q.setFromRes(re.dFrom);
        q.setToRes(re.dTo);
        re.rQuery.add(q);

        resList = iResOp.queryReservation(getH(HOTEL), re.rQuery);
        assertFalse(resList.isEmpty());
        assertEquals(1, resList.size());

        ReservationForm r = iRes.findElem(getH(HOTEL), re.rese);
        iRes.deleteElem(getH(HOTEL), r);
        resList = iResOp.queryReservation(getH(HOTEL), re.rQuery);
        assertTrue(resList.isEmpty());
    }

    @Test
    public void test3() {
        R re = createRese();

        List<ResData> resList = iResOp.queryReservation(getH(HOTEL), re.rQuery);
        assertFalse(resList.isEmpty());
        assertEquals(2, resList.size());
        ReservationForm r = iRes.findElem(getH(HOTEL), re.rese);
        r.setStatus(ResStatus.STAY);
        iGetI.invalidateCache();
        iRes.changeElem(getH1(HOTEL), r);
        r = iRes.findElem(getH(HOTEL), re.rese);
        assertEquals("user", r.getCreationPerson());
        assertNotNull(r.getCreationDate());
        assertEquals("modifuser", r.getModifPerson());
        assertNotNull(r.getModifDate());
        assertEquals(ResStatus.STAY, r.getStatus());
        resList = iResOp.queryReservation(getH(HOTEL), re.rQuery);
        assertFalse(resList.isEmpty());
        assertEquals(2, resList.size());
    }

    @Test
    public void test4() {
        R re = createRese();
        List<ResQuery>rQuery = new ArrayList<ResQuery>();
        ResQuery q = new ResQuery();
        q.setRoomName("R10");
        q.setFromRes(re.dFrom);
        q.setToRes(re.dFrom);
        rQuery.add(q);
        List<ResData> resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertEquals(1,resList.size());
        
        HotelServices ho = new HotelServices();
        ho.setName("beef");
        ho.setDescription("Restaurant");
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        ho.setServiceType(ServiceType.OTHER);
        iServices.addElem(getH(HOTEL), ho);

        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();
        
        ReservationPaymentDetail add = new ReservationPaymentDetail();
        add.setDescription("beef");
        add.setGuestName(guest1);
        add.setPrice(new BigDecimal(100));
        add.setPriceList(new BigDecimal(150.00));
        add.setPriceTotal(new BigDecimal(200.0));
        add.setQuantity(2);
        add.setRoomName("R10");
        add.setServDate(re.dFrom);
        add.setService("beef");
        add.setVat("22%");
        iResOp.addResAddPayment(getH(HOTEL), re.rese, add);
        
        resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertEquals(1,resList.size());

    }

}
