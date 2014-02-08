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

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;

public class Test14 extends TestHelper {

    @Before
    public void before() {
        clearObjects();
        createHotels();
        DateFormatUtil.setTestToday(DateFormatUtil.toD(2013, 6, 13));
    }

    @Test
    public void test1() {
        HotelServices ho = new HotelServices();
        ho.setName("beef");
        ho.setDescription("Restaurant");
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        ho.setServiceType(ServiceType.OTHER);
        iServices.addElem(getH(HOTEL), ho);
        ho = iServices.findElem(getH(HOTEL), "beef");
        assertNotNull(ho);
        assertEquals(ServiceType.OTHER, ho.getServiceType());
    }

    @Test
    public void test2() {
        test1();
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(3);
        det.setPrice(new BigDecimal("100.0"));
        det.setPriceList(new BigDecimal("200.0"));
        det.setRoomName("P10");
        det.setResDate(toDate(2013, 4, 10));
        r.getResDetail().add(det);
        r = iRes.addElem(getH(HOTEL), r);
        String sym = r.getName();

        r = iRes.findElem(getH(HOTEL), sym);
        assertNotNull(r);
        det = r.getResDetail().get(0);
        assertEqB(100.0, det.getPrice());
        assertEqB(200.0, det.getPriceList());

        System.out.println(det.getNoChildren());
        assertEquals(-1, det.getNoChildren());
        assertEquals(-1, det.getNoExtraBeds());
    }

    @Test
    public void test3() {
        test1();
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(3);
        det.setPrice(new BigDecimal("100.0"));
        det.setPriceList(new BigDecimal("200.0"));
        // add additional here
        det.setNoChildren(5);
        det.setPriceChildren(new BigDecimal("1.1"));
        det.setPriceListChildren(new BigDecimal("2.2"));
        det.setNoExtraBeds(11);
        det.setPriceExtraBeds(new BigDecimal("9.9"));
        det.setPriceListExtraBeds(new BigDecimal("11.11"));

        det.setRoomName("P10");
        det.setResDate(toDate(2013, 4, 10));
        r.getResDetail().add(det);
        r = iRes.addElem(getH(HOTEL), r);
        String sym = r.getName();

        r = iRes.findElem(getH(HOTEL), sym);
        assertNotNull(r);
        det = r.getResDetail().get(0);
        assertEqB(100.0, det.getPrice());
        assertEqB(200.0, det.getPriceList());
        assertEquals(5, det.getNoChildren());
        assertEqB(1.1, det.getPriceChildren());
        assertEqB(2.2, det.getPriceListChildren());
        assertEquals(11, det.getNoExtraBeds());
        assertEqB(9.9, det.getPriceExtraBeds());
        assertEqB(11.11, det.getPriceListExtraBeds());
        
        assertTrue(det.isPerperson());
        
        det.setPerperson(false);
        iRes.changeElem(getH(HOTEL), r);
        r = iRes.findElem(getH(HOTEL), sym);
        assertNotNull(r);
        det = r.getResDetail().get(0);
        assertFalse(det.isPerperson());
    }

}
