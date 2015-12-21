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

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;

public class Test21 extends TestHelper {

    private String runtest() {
        HotelPriceList pr = new HotelPriceList();
        pr.setName("p1");
        pr.setFromDate(toDate(2010, 10, 1));
        iPrice.addElem(getH(HOTEL), pr);

        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);

        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(3);
        det.setPrice(new BigDecimal("100.0"));
        det.setPriceTotal(new BigDecimal("100.0"));
        det.setPriceList(new BigDecimal("200.0"));
        det.setRoomName("P10");
        det.setResDate(toDate(2013, 4, 10));
        det.setPriceListName("p1");
        det.setVat("7%");
        r.getResDetail().add(det);
        r = iRes.addElem(getH(HOTEL), r);
        String sym = r.getName();
        assertNotNull(sym);
        r = iRes.findElem(getH(HOTEL), sym);
        det = r.getResDetail().get(0);
        assertEquals("p1", det.getPriceListName());
        return sym;
    }

    @Test
    public void test1() {
        runtest();
    }

    @Test
    public void test2() {
        String sym = runtest();
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        assertNotNull(r);
        ReservationPaymentDetail det = r.getResDetail().get(0);
        det.setPriceTotal(new BigDecimal("199.0"));
        iRes.changeElem(getH(HOTEL), r);
        r = iRes.findElem(getH(HOTEL), sym);
        det = r.getResDetail().get(0);
        assertEqB(100.0, det.getPrice());
        assertEqB(199.0, det.getPriceTotal());
    }

}
