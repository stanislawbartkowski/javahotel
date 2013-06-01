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

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.shared.IHotelConsts;

public class Test7 extends TestHelper {

    private void resetHotel() {
        createHotels();
        iPrice.deleteAll(HOTEL);
        iPrice.deleteAll(HOTEL1);

    }

    @Test
    public void test1() {
        resetHotel();
        HotelPriceList p = new HotelPriceList();
        p.setName("p1");
        p.setAttrSqlDate(IHotelConsts.PRICELISTFROMPROP, toDate(2010, 10, 1));
        iPrice.addElem(HOTEL1, p);
        List<HotelPriceList> pList = iPrice.getList(HOTEL);
        assertTrue(pList.isEmpty());
        pList = iPrice.getList(HOTEL1);
        assertEquals(1, pList.size());
        p = pList.get(0);
        assertTrue(eqDate(p.getAttrSqlDate(IHotelConsts.PRICELISTFROMPROP),
                2010, 10, 1));
        assertNull(p.getAttrSqlDate(IHotelConsts.PRICELISTTOPROP));
    }

    @Test
    public void test2() {
        resetHotel();
        HotelPriceList p = new HotelPriceList();
        p.setName("p1");
        p.setAttrSqlDate(IHotelConsts.PRICELISTFROMPROP, toDate(2010, 10, 1));
        iPrice.addElem(HOTEL1, p);
        List<HotelPriceList> pList = iPrice.getList(HOTEL1);
        p = pList.get(0);
        p.setAttrSqlDate(IHotelConsts.PRICELISTTOPROP, toDate(2012, 1, 1));
        iPrice.changeElem(HOTEL1, p);
        pList = iPrice.getList(HOTEL1);
        p = pList.get(0);
        assertTrue(eqDate(p.getAttrSqlDate(IHotelConsts.PRICELISTFROMPROP),
                2010, 10, 1));
        assertTrue(eqDate(p.getAttrSqlDate(IHotelConsts.PRICELISTTOPROP), 2012,
                1, 1));
    }

    @Test
    public void test3() {
        resetHotel();
        for (int i = 0; i < 100; i++) {
            HotelPriceList p = new HotelPriceList();
            p.setName("p" + i);
            p.setAttrSqlDate(IHotelConsts.PRICELISTFROMPROP,
                    toDate(2010, 10, 1));
            iPrice.addElem(HOTEL1, p);
        }
        List<HotelPriceList> pList = iPrice.getList(HOTEL1);
        assertEquals(100, pList.size());
        HotelPriceList p = pList.get(10);
        String dName = p.getName();
        iPrice.deleteElem(HOTEL1, p);
        pList = iPrice.getList(HOTEL1);
        assertEquals(99, pList.size());
        for (HotelPriceList pp : pList) {
            assertFalse(pp.getName().equals(dName));
        }
    }
}
