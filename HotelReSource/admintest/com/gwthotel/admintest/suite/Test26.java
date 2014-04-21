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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.payment.PaymentBill;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.stay.ResGuest;


public class Test26 extends TestHelper {
    
    @Before
    public void before() {
        clearObjects();
        createHotels();
    }
    
    
    private CustomerBill createP() {

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
        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(3);
        det.setPrice(new BigDecimal("100.0"));
        det.setPriceTotal(new BigDecimal("100.0"));
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
        return b;
    }

    @Test
    public void test1() {
        Long l = iClear.numberOf(getH(HOTEL), HotelObjects.ROOM);
        System.out.println(l);
        assertEquals(new Long(0),l);
        for (HotelObjects o :HotelObjects.values()) {
            l = iClear.numberOf(getH(HOTEL), o);
            System.out.println(o + " " + l);
            assertEquals(new Long(0),l);            
        }
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        l = iClear.numberOf(getH(HOTEL), HotelObjects.ROOM);
        assertEquals(new Long(1),l);
        iClear.clearObjects(getH(HOTEL1));
        l = iClear.numberOf(getH(HOTEL), HotelObjects.ROOM);
        assertEquals(new Long(1),l);
        iClear.clearObjects(getH(HOTEL));
        l = iClear.numberOf(getH(HOTEL), HotelObjects.ROOM);
        assertEquals(new Long(0),l);
    }
    
    @Test
    public void test2() {
        CustomerBill b = createP();
        Long l = iClear.numberOf(getH(HOTEL), HotelObjects.PAYMENTS);
        System.out.println(l);
        assertEquals(new Long(0),l);
        PaymentBill p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(100.0));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        l = iClear.numberOf(getH(HOTEL), HotelObjects.PAYMENTS);
        System.out.println(l);
        assertEquals(new Long(1),l);
        iClear.clearObjects(getH(HOTEL));
        l = iClear.numberOf(getH(HOTEL), HotelObjects.PAYMENTS);
        System.out.println(l);
        assertEquals(new Long(0),l);
    }
    
    @Test
    public void test3() {
        CustomerBill b = createP();
        Long l = iClear.numberOf(getH(HOTEL), HotelObjects.PAYMENTS);
        System.out.println(l);
        assertEquals(new Long(0),l);
        PaymentBill p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(100.0));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        l = iClear.numberOf(getH(HOTEL), HotelObjects.PAYMENTS);
        System.out.println(l);
        assertEquals(new Long(1),l);
        
        String rese = b.getReseName();
        ReservationForm r = iRes.findElem(getH(HOTEL), rese);
        iRes.deleteElem(getH(HOTEL), r);
        l = iClear.numberOf(getH(HOTEL), HotelObjects.PAYMENTS);
        System.out.println(l);
        assertEquals(new Long(0),l);        
    }
    
    @Test
    public void test4() {
        CustomerBill b = createP();
        Long l = iClear.numberOf(getH(HOTEL), HotelObjects.GUESTS);
        System.out.println(l);
        assertEquals(new Long(0),l);
//        void setResGuestList(HotelId hotel, String resName, List<ResGuest> gList);
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p.setAttr("country", "gb");
        p = iCustomers.addElem(getH(HOTEL), p);
        ResGuest g = new ResGuest();
        g.setGuestName(p.getName());
        g.setRoomName("P10");
        List<ResGuest> gL = new ArrayList<ResGuest>();
        gL.add(g);
        iResOp.setResGuestList(getH(HOTEL), b.getReseName(),gL);
        l = iClear.numberOf(getH(HOTEL), HotelObjects.GUESTS);
        System.out.println(l);
        assertEquals(new Long(1),l);
        iClear.clearObjects(getH(HOTEL));
        l = iClear.numberOf(getH(HOTEL), HotelObjects.GUESTS);
        System.out.println(l);
        assertEquals(new Long(0),l);
        l = iClear.numberOf(getH(HOTEL),HotelObjects.RESERVATIONDETAILS);
        assertEquals(new Long(0),l);                
    }
    
    @Test
    public void test5() {
        Long l = iClear.numberOf(getH(HOTEL),HotelObjects.RESERVATIONDETAILS);
        assertEquals(new Long(0),l);        
        CustomerBill b = createP();
        l = iClear.numberOf(getH(HOTEL), HotelObjects.RESERVATIONDETAILS);
        System.out.println(l);
        assertEquals(new Long(1),l);
        iClear.clearObjects(getH(HOTEL));
        l = iClear.numberOf(getH(HOTEL),HotelObjects.RESERVATIONDETAILS);
        assertEquals(new Long(0),l);        
    }
    


}
