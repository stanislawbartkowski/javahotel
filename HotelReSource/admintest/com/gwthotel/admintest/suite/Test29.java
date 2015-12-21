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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogVariables;

public class Test29 extends TestHelper {

    @Test
    public void test1() {
        ResQuery r = new ResQuery();
        r.setFromRes(toDate(2014, 2, 1));
        r.setToRes(toDate(2014, 2, 2));
        List<ResData> l = iResOp.searchReservation(getH(HOTEL), r);
        assertTrue(l.isEmpty());
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        l = iResOp.searchReservation(getH(HOTEL), r);
        assertEquals(1, l.size());
        assertEquals("P10", l.get(0).getRoomName());
    }

    @Test
    public void test2() {
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        ho = new HotelRoom();
        ho.setName("P11");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        ho = new HotelRoom();
        ho.setName("P12");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        ResQuery r = new ResQuery();
        r.setFromRes(toDate(2014, 2, 1));
        r.setToRes(toDate(2014, 2, 2));
        List<ResData> l = iResOp.searchReservation(getH(HOTEL), r);
        assertEquals(3, l.size());
        
        HotelServices se = new HotelServices();
        se.setName("1p1");
        se.setNoPersons(1);
        se.setVat("7%");
        iServices.addElem(getH(HOTEL), se);
        HotelCustomer cust = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        cust = iCustomers.addElem(getH(HOTEL), cust);
        ReservationForm re = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        re.setCustomerName(cust.getName());

        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(1);
        det.setRoomName("P10");
        det.setResDate(toDate(2014,2,1));
        det.setPrice(new BigDecimal(100));
        det.setService("1p1");
        det.setPriceTotal(new BigDecimal(100));
        re.getResDetail().add(det);
        re = iRes.addElem(getH(HOTEL), re);
        System.out.println(re.getName());
        assertNotNull(re.getName());
        l = iResOp.searchReservation(getH(HOTEL), r);
        assertEquals(2, l.size());
        String name = null;
        for (ResData res : l) {
            System.out.println(res.getRoomName());
            if (res.getRoomName().equals("P10")) name= "P10";
        }
        assertNull(name);
        
        ReservationForm re1 = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        re1.setCustomerName(cust.getName());
        det = new ReservationPaymentDetail();
        det.setNoP(1);
        det.setRoomName("P11");
        det.setResDate(toDate(2014,2,2));
        det.setPrice(new BigDecimal(100));
        det.setService("1p1");
        det.setPriceTotal(new BigDecimal(100));
        re1.getResDetail().add(det);
        re1 = iRes.addElem(getH(HOTEL), re1);
        assertNotNull(re1);
        l = iResOp.searchReservation(getH(HOTEL), r);
        assertEquals(2, l.size());
        
        iResOp.changeStatus(getH(HOTEL), re.getName(), ResStatus.CANCEL);
        l = iResOp.searchReservation(getH(HOTEL), r);
        assertEquals(3, l.size());
        
        r.setFromRes(toDate(2014, 2, 1));
        r.setToRes(toDate(2014, 2, 3));
        l = iResOp.searchReservation(getH(HOTEL), r);
        assertEquals(2, l.size());

    }
    
    @Test
    public void test3() {
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        test2();
        DialogVariables v = new DialogVariables();
        runAction(token, v, "dialog7.xml", "testsearch");
        assertOK(v);
    }
    


}
