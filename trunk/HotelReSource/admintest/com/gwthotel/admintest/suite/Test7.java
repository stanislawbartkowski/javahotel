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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.pricelist.HotelPriceList;

public class Test7 extends TestHelper {

    @Before
    public void resetHotel() {
        clearObjects();
        createHotels();
    }

    @Test
    public void test1() {
        HotelPriceList p = new HotelPriceList();
        p.setName("p1");
        p.setFromDate(toDate(2010, 10, 1));
        iPrice.addElem(getH(HOTEL1), p);
        List<HotelPriceList> pList = iPrice.getList(getH(HOTEL));
        assertTrue(pList.isEmpty());
        pList = iPrice.getList(getH(HOTEL1));
        assertEquals(1, pList.size());
        p = pList.get(0);
        assertTrue(eqDate(p.getFromDate(), 2010, 10, 1));
        assertNull(p.getToDate());
    }

    @Test
    public void test2() {
        HotelPriceList p = new HotelPriceList();
        p.setName("p1");
        p.setFromDate(toDate(2010, 10, 1));
        iPrice.addElem(getH(HOTEL1), p);
        List<HotelPriceList> pList = iPrice.getList(getH(HOTEL1));
        p = pList.get(0);
        p.setToDate(toDate(2012, 1, 1));
        iPrice.changeElem(getH(HOTEL1), p);
        pList = iPrice.getList(getH(HOTEL1));
        p = pList.get(0);
        assertTrue(eqDate(p.getFromDate(), 2010, 10, 1));
        assertTrue(eqDate(p.getToDate(), 2012, 1, 1));
    }

    @Test
    public void test3() {
        for (int i = 0; i < 100; i++) {
            HotelPriceList p = new HotelPriceList();
            p.setName("p" + i);
            p.setFromDate(toDate(2010, 10, 1));
            iPrice.addElem(getH(HOTEL1), p);
        }
        List<HotelPriceList> pList = iPrice.getList(getH(HOTEL1));
        assertEquals(100, pList.size());
        HotelPriceList p = pList.get(10);
        String dName = p.getName();
        iPrice.deleteElem(getH(HOTEL1), p);
        pList = iPrice.getList(getH(HOTEL1));
        assertEquals(99, pList.size());
        for (HotelPriceList pp : pList) {
            assertFalse(pp.getName().equals(dName));
        }
    }
}
