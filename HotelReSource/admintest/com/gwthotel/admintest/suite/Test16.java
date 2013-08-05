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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.hotel.services.ServiceType;
import com.gwthotel.hotel.stay.ResAddPayment;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;

public class Test16 extends TestHelper {

    private final static String SERVICE1 = "beef";
    private final static String RESNAME = "2013/R/2";

    @Before
    public void before() {
        clearObjects();
        createHotels();
        DateUtil.setTestToday(DateFormatUtil.toD(2013, 6, 13));
    }

    @Test
    public void test1() {
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setName(RESNAME);
        r = iRes.addElem(getH(HOTEL), r);
        System.out.println(r.getName());

        assertNotNull(r.getName());
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);

        HotelServices se = new HotelServices();
        se.setName(SERVICE1);
        se.setDescription("Restaurant");
        se.setAttr(IHotelConsts.VATPROP, "7%");
        se.setServiceType(ServiceType.OTHER);
        iServices.addElem(getH(HOTEL), se);
        se = iServices.findElem(getH(HOTEL), "beef");

        p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();

        ResAddPayment add = new ResAddPayment();
        add.setDescription("Beverage");
        add.setGuestName(guest1);
        add.setPrice(new BigDecimal(100));
        add.setPriceList(new BigDecimal(150.00));
        add.setPriceTotal(new BigDecimal(200.0));
        add.setQuantity(2);
        add.setRoomName("P10");
        add.setServDate(toDate(2013, 4, 5));
        add.setServiceName(SERVICE1);
        iResOp.AddResAddPayment(getH(HOTEL), RESNAME, add);

        List<ResAddPayment> aList = iResOp.getRedAddPaymentList(getH(HOTEL),
                RESNAME);
        assertEquals(1, aList.size());
        add = aList.get(0);
        assertEquals("Beverage", add.getDescription());
        assertEquals(guest1, add.getGuestName());
        assertEquals(new BigDecimal(100), add.getPrice());
        assertEquals(new BigDecimal(150), add.getPriceList());
        assertEquals(new BigDecimal(200), add.getPriceTotal());
        assertEquals(2, add.getQuantity());
        assertEquals(toDate(2013, 4, 5), add.getServDate());
        assertEquals(SERVICE1, add.getServiceName());
    }

    @Test
    public void test2() {
        test1();
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();

        for (int i = 0; i < 100; i++) {
            ResAddPayment add = new ResAddPayment();
            add.setDescription("Beverage");
            add.setGuestName(guest1);
            add.setPrice(new BigDecimal(100));
            add.setPriceList(new BigDecimal(150.00));
            add.setPriceTotal(new BigDecimal(200.0));
            add.setQuantity(2);
            add.setRoomName("P10");
            add.setServDate(toDate(2013, 4, 5));
            iResOp.AddResAddPayment(getH(HOTEL), RESNAME, add);
        }
        List<ResAddPayment> aList = iResOp.getRedAddPaymentList(getH(HOTEL),
                RESNAME);
        assertEquals(101, aList.size());
    }

}
