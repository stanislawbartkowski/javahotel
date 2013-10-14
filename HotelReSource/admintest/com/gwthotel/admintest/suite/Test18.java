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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;

public class Test18 extends TestHelper {

    @Before
    public void before() {
        clearObjects();
        createHotels();
        DateUtil.setTestToday(DateFormatUtil.toD(2013, 6, 13));
    }

    @Test
    public void test1() {

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

        CustomerBill b = (CustomerBill) hObjects.construct(getH(HOTEL),
                HotelObjects.BILL);
        b.setGensymbol(true);
        b.setPayer(p.getName());
        b.setReseName(sym);
        b.setIssueDate(toDate(2010, 10, 12));
        for (ReservationPaymentDetail d : r.getResDetail()) {
            b.getPayList().add(d.getId());
        }
        b = iBills.addElem(getH(HOTEL), b);
        assertNotNull(b);
        System.out.println(b.getName());
        String bsym = b.getName();
        b = iBills.findElem(getH(HOTEL), bsym);
        assertNotNull(b);
        assertEquals(sym, b.getReseName());
        assertEquals(p.getName(), b.getPayer());
        assertEquals(1, b.getPayList().size());
        for (Long l : b.getPayList()) {
            System.out.println(l);
            assertNotNull(l);
        }
    }

    @Test
    public void test2() {
        test1();
        List<CustomerBill> bL = iBills.getList(getH(HOTEL));
        assertEquals(1, bL.size());
        iBills.deleteElem(getH(HOTEL), bL.get(0));
        bL = iBills.getList(getH(HOTEL));
        assertTrue(bL.isEmpty());
    }

    @Test
    public void test3() {
        test1();
        List<CustomerBill> bL = iBills.getList(getH(HOTEL));
        assertEquals(1, bL.size());
        CustomerBill b = bL.get(0);
        b.getPayList().add(new Long(100));
        iBills.changeElem(getH(HOTEL), b);
        b = iBills.findElem(getH(HOTEL), b.getName());
        assertEquals(2, b.getPayList().size());
    }

    @Test
    public void test4() {
        test1();
        List<CustomerBill> bL = iBills.getList(getH(HOTEL));
        assertEquals(1, bL.size());
        CustomerBill b = bL.get(0);
        String resName = b.getReseName();
        List<CustomerBill> bList = iResOp.findBillsForReservation(getH(HOTEL),
                resName);
        assertEquals(1, bList.size());
        HotelCustomer cust = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        cust.setGensymbol(true);
        cust = iCustomers.addElem(getH(HOTEL), cust);

        b = (CustomerBill) hObjects.construct(getH(HOTEL), HotelObjects.BILL);
        b.setGensymbol(true);
        b.setPayer(cust.getName());
        b.setReseName(resName);
        b.setIssueDate(toDate(2010, 10, 13));
        iBills.addElem(getH(HOTEL), b);
        bList = iResOp.findBillsForReservation(getH(HOTEL), resName);
        assertEquals(2, bList.size());

        b = bList.get(0);
        System.out.println(b.getCreationPerson());
        assertEquals("user", b.getCreationPerson());
        assertEquals("user", b.getModifPerson());
        assertNotNull(b.getCreationDate());
        assertNotNull(b.getModifDate());
        eqDate(b.getIssueDate(), 2010, 10, 12);
    }

    @Test
    public void test5() {
        test4();
        iClear.clearObjects(getH(HOTEL));
        List<ReservationForm> rList = iRes.getList(getH(HOTEL));
        assertTrue(rList.isEmpty());
        List<CustomerBill> bL = iBills.getList(getH(HOTEL));
        assertTrue(bL.isEmpty());
        List<HotelCustomer> cList = iCustomers.getList(getH(HOTEL));
        assertTrue(cList.isEmpty());
        List<HotelRoom> roList = iRooms.getList(getH(HOTEL));
        assertTrue(roList.isEmpty());
        List<HotelServices> seList = iServices.getList(getH(HOTEL));
        assertTrue(seList.isEmpty());
        List<HotelPriceList> prList = iPrice.getList(getH(HOTEL));
        assertTrue(prList.isEmpty());
    }
}
