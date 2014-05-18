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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.stay.ResGuest;

public class Test32 extends TestHelper {

    @Test
    public void test1() {
        HotelPriceList pr = new HotelPriceList();
        pr.setName("pr1");
        iPrice.addElem(getH(HOTEL), pr);
        List<String> li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORPRICELIST, "pr1");
        assertTrue(li.isEmpty());
        CustomerBill b = createP();
        li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORPRICELIST, "pr1");
        assertTrue(li.isEmpty());
        ReservationForm r = iRes.findElem(getH(HOTEL), b.getReseName());
        assertNotNull(r);
        r.getResDetail().get(0).setPriceListName("pr1");
        iRes.changeElem(getH(HOTEL), r);
        li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORPRICELIST, "pr1");
        assertEquals(1, li.size());
    }

    @Test
    public void test2() {
        HotelCustomer cust = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        cust.setGensymbol(true);
        cust = iCustomers.addElem(getH(HOTEL), cust);
        assertNotNull(cust);
        List<String> li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORCUSTOMER, cust.getName());
        assertTrue(li.isEmpty());
        CustomerBill b = createP();
        ReservationForm r = iRes.findElem(getH(HOTEL), b.getReseName());
        assertNotNull(r);
        li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORCUSTOMER, r.getCustomerName());
        assertEquals(1, li.size());
    }

    @Test
    public void test3() {
        HotelCustomer cust = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        cust.setGensymbol(true);
        cust = iCustomers.addElem(getH(HOTEL), cust);
        assertNotNull(cust);
        List<String> li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORGUEST, cust.getName());
        assertTrue(li.isEmpty());
        CustomerBill b = createP();
        ReservationForm r = iRes.findElem(getH(HOTEL), b.getReseName());
        assertNotNull(r);
        li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORGUEST, r.getCustomerName());
        assertTrue(li.isEmpty());

        HotelRoom ro = new HotelRoom();
        ro.setName("SUPER");
        iRooms.addElem(getH(HOTEL), ro);

        List<ResGuest> l = new ArrayList<ResGuest>();
        ResGuest reg = new ResGuest();
        reg.setRoomName("SUPER");
        reg.setGuestName(cust.getName());
        l.add(reg);
        iResOp.setResGuestList(getH(HOTEL), r.getName(), l);
        li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORGUEST, cust.getName());
        assertEquals(1, li.size());
    }
    
    @Test
    public void test4() {
        HotelCustomer cust = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        cust.setGensymbol(true);
        cust = iCustomers.addElem(getH(HOTEL), cust);
        assertNotNull(cust);
        List<String> li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORPAYER, cust.getName());
        assertTrue(li.isEmpty());
        CustomerBill b = createP();
        ReservationForm r = iRes.findElem(getH(HOTEL), b.getReseName());
        assertNotNull(r);
        li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORPAYER, r.getCustomerName());
        assertEquals(1, li.size());
        li = iResOp.getReseForInfoType(getH(HOTEL),
                IReservationOp.ResInfoType.FORPAYER, cust.getName());
        assertTrue(li.isEmpty());
    }

}
