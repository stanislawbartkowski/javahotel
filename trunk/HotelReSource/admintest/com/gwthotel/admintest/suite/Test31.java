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
package com.gwthotel.admintest.suite;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;

public class Test31 extends TestHelper {

    @Test
    public void test1() {
        HotelServices ho = new HotelServices();
        ho.setName("1p1");
        ho.setDescription("One person in one person room");
        ho.setNoPersons(2);
        ho.setNoChildren(3);
        ho.setNoExtraBeds(4);
        ho.setPerperson(false);
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        ho = iServices.addElem(getH(HOTEL), ho);

        List<String> li = iResOp.getReseForService(getH(HOTEL), ho.getName());
        assertTrue(li.isEmpty());
    }

    @Test
    public void test2() {
        HotelServices se = new HotelServices();
        se.setName("1p1");
        se.setDescription("One person in one person room");
        se.setNoPersons(2);
        se.setNoChildren(3);
        se.setNoExtraBeds(4);
        se.setPerperson(false);
        se.setAttr(IHotelConsts.VATPROP, "7%");
        se = iServices.addElem(getH(HOTEL), se);
        se = new HotelServices();
        se.setName("1p2");
        se.setDescription("One person in one person room");
        se.setNoPersons(2);
        se.setNoChildren(3);
        se.setNoExtraBeds(4);
        se.setPerperson(false);
        se.setAttr(IHotelConsts.VATPROP, "7%");
        se = iServices.addElem(getH(HOTEL), se);

        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p.setAttr("country", "gb");
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        ReservationPaymentDetail det1 = new ReservationPaymentDetail();
        det1.setNoP(3);
        det1.setPrice(new BigDecimal("100.0"));
        det1.setPriceTotal(new BigDecimal("100.0"));
        det1.setPriceList(new BigDecimal("200.0"));
        det1.setRoomName("P10");
        det1.setResDate(toDate(2013, 4, 10));
        det1.setService("1p1");
        r.getResDetail().add(det1);

        ReservationPaymentDetail det2 = new ReservationPaymentDetail();
        det2.setNoP(3);
        det2.setPrice(new BigDecimal("100.0"));
        det2.setPriceTotal(new BigDecimal("100.0"));
        det2.setPriceList(new BigDecimal("200.0"));
        det2.setRoomName("P10");
        det2.setResDate(toDate(2013, 4, 10));
        det2.setService("1p2");
        r.getResDetail().add(det2);
        r = iRes.addElem(getH(HOTEL), r);

        List<String> li = iResOp.getReseForService(getH(HOTEL), "1p1");
        assertEquals(1, li.size());
        assertEquals(r.getName(), li.get(0));

        ReservationForm r1 = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r1.setCustomerName(p.getName());
        r1.setGensymbol(true);
        r1.getResDetail().add(det2);
        r1 = iRes.addElem(getH(HOTEL), r1);

        li = iResOp.getReseForService(getH(HOTEL), "1p1");
        assertEquals(1, li.size());
        assertEquals(r.getName(), li.get(0));

        li = iResOp.getReseForService(getH(HOTEL), "1p2");
        assertEquals(2, li.size());
    }

    @Test
    public void test3() {
        test2();
        List<String> li = iResOp.getReseForRoom(getH(HOTEL), "P10");
        assertEquals(2, li.size());

        HotelRoom ho = new HotelRoom();
        ho.setName("P12");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        
        li = iResOp.getReseForRoom(getH(HOTEL), "P12");
        assertTrue(li.isEmpty());
    }

}
