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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;

public class Test12 extends TestHelper {

    @Before
    public void before() {
        createHotels();
        iCustomers.deleteAll(getH(HOTEL));
        iCustomers.deleteAll(getH(HOTEL1));
        iRooms.deleteAll(getH(HOTEL));
        iRooms.deleteAll(getH(HOTEL1));
        iServices.deleteAll(getH(HOTEL));
        iServices.deleteAll(getH(HOTEL1));
        iHGen.clearAll(getH(HOTEL));
        iHGen.clearAll(getH(HOTEL1));
        DateUtil.setTestToday(DateFormatUtil.toD(2013,6,13));
        iRes.deleteAll(getH(HOTEL));
        iRes.deleteAll(getH(HOTEL1));
    }
    
    @Test
    public void test1() {
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL), HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm)hObjects.construct(getH(HOTEL), HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        r = iRes.addElem(getH(HOTEL), r);
        System.out.println(r.getName());
        assertNotNull(r.getName());
        List<ReservationForm> rList = iRes.getList(getH(HOTEL));
        assertEquals(1,rList.size());
        r = rList.get(0);
        assertNotNull(r.getName());
        assertEquals(p.getName(),r.getCustomerName());
    }

}
