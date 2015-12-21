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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.stay.ResGuest;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;

public class Test15 extends TestHelper {

    @Before
    public void before() {
        clearObjects();
        createHotels();
        setTestToday(DateFormatUtil.toD(2013, 6, 13));
    }

    @Test
    public void test1() {
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r = iRes.addElem(getH(HOTEL), r);
        System.out.println(r.getName());
        String resName = r.getName();

        assertNotNull(r.getName());
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);

        p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();
        p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        String guest2 = iCustomers.addElem(getH(HOTEL), p).getName();

        List<ResGuest> gList = new ArrayList<ResGuest>();
        ResGuest g = new ResGuest();
        g.setGuestName(guest1);
        g.setRoomName("P10");
        gList.add(g);
        iResOp.setResGuestList(getH(HOTEL), resName, gList);

        g = iResOp.getResGuestList(getH(HOTEL), resName).get(0);
        assertEquals("P10", g.getRoomName());
        assertEquals(guest1, g.getGuestName());

        gList = new ArrayList<ResGuest>();
        g = new ResGuest();
        g.setGuestName(guest2);
        g.setRoomName("P10");
        gList.add(g);
        iResOp.setResGuestList(getH(HOTEL), resName, gList);

        gList = iResOp.getResGuestList(getH(HOTEL), resName);
        assertEquals(1, gList.size());
        g = gList.get(0);
        assertEquals("P10", g.getRoomName());
        assertEquals(guest2, g.getGuestName());
    }

    @Test
    public void test2() {
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r = iRes.addElem(getH(HOTEL), r);
        System.out.println(r.getName());
        String resName = r.getName();

        assertNotNull(r.getName());
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);

        p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        String guest1 = iCustomers.addElem(getH(HOTEL), p).getName();

        List<ResGuest> gList = new ArrayList<ResGuest>();
        ResGuest g = new ResGuest();
        g.setGuestName(guest1);
        g.setRoomName("P10");
        gList.add(g);
        iResOp.setResGuestList(getH(HOTEL), resName, gList);

        g = iResOp.getResGuestList(getH(HOTEL), resName).get(0);
        assertEquals("P10", g.getRoomName());
        assertEquals(guest1, g.getGuestName());

        gList = new ArrayList<ResGuest>();
        for (int i = 0; i < 100; i++) {
            p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                    HotelObjects.CUSTOMER);
            String guest2 = iCustomers.addElem(getH(HOTEL), p).getName();
            g = new ResGuest();
            g.setGuestName(guest2);
            g.setRoomName("P10");
            gList.add(g);
        }
        iResOp.setResGuestList(getH(HOTEL), resName, gList);

        gList = iResOp.getResGuestList(getH(HOTEL), resName);
        assertEquals(100, gList.size());

    }
}
