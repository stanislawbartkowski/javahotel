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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationDetail;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;

public class Test13 extends TestHelper {
    
    @Before
    public void before() {
        clearObjects();
        createHotels();
        DateUtil.setTestToday(DateFormatUtil.toD(2013, 6, 13));
    }
    
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
        
        ReservationDetail det = new ReservationDetail();
        det.setNoP(1);
        det.setResDate(d);
        det.setRoom("R10");
        det.setPrice(new BigDecimal(100));
        
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
        iResOp.changeStatus(getH(HOTEL),r.getName(),ResStatus.CANCEL);
        resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertTrue(resList.isEmpty());        
    }

    @Test
    public void test2() {
        Date dFrom = DateFormatUtil.toD(2013, 6, 12);
        Date dTo = DateFormatUtil.toD(2013, 6, 15);
        List<ResQuery> rQuery = new ArrayList<ResQuery>();
        ResQuery q = new ResQuery();
        q.setRoomName("R10");
        q.setFromRes(dFrom);
        q.setToRes(dTo);
        rQuery.add(q);
        q = new ResQuery();
        q.setRoomName("R11");
        q.setFromRes(dFrom);
        q.setToRes(dTo);
        rQuery.add(q);
        List<ResData> resList = iResOp.queryReservation(getH(HOTEL), rQuery);
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
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        ReservationDetail det = new ReservationDetail();
        det.setNoP(1);
        det.setResDate(dFrom);
        det.setRoom("R10");
        det.setPrice(new BigDecimal(100));
        r.getResDetail().add(det);
        det = new ReservationDetail();
        det.setNoP(1);
        det.setResDate(dTo);
        det.setRoom("R11");
        det.setPrice(new BigDecimal(100));
        r.getResDetail().add(det);
        r = iRes.addElem(getH(HOTEL), r);
        
        resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertFalse(resList.isEmpty());
        assertEquals(2,resList.size());

        rQuery = new ArrayList<ResQuery>();
        q = new ResQuery();
        q.setRoomName("R10");
        q.setFromRes(dFrom);
        q.setToRes(dTo);
        rQuery.add(q);

        resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertFalse(resList.isEmpty());
        assertEquals(1,resList.size());

        iRes.deleteElem(getH(HOTEL), r);
        resList = iResOp.queryReservation(getH(HOTEL), rQuery);
        assertTrue(resList.isEmpty());
    }

}
