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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;

public class Test6 extends TestHelper {

    private void resetHotel() {
        createHotels();
        iServices.deleteAll(HOTEL);
        iServices.deleteAll(HOTEL1);

    }

    @Test
    public void test1() {
        resetHotel();
        HotelServices ho = new HotelServices();
        ho.setName("1p1");
        ho.setDescription("One person in one person room");
        ho.setNoPersons(2);
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        iServices.addElem(HOTEL, ho);

        List<HotelServices> hList = iServices.getList(HOTEL);
        assertEquals(1, hList.size());
        ho = hList.get(0);
        assertEquals("1p1", ho.getName());
        assertEquals(HOTEL, ho.getAttr(IHotelConsts.HOTELPROP));
        assertEquals("One person in one person room", ho.getDescription());
        assertEquals(2, ho.getNoPersons());
        assertEquals("7%", ho.getAttr(IHotelConsts.VATPROP));

        hList = iServices.getList(HOTEL1);
        assertEquals(0, hList.size());
    }

    private void addList(String hotel, int no) {
        for (int i = 0; i < no; i++) {
            HotelServices ho = new HotelServices();
            ho.setName("" + i);
            ho.setDescription(hotel + "Desc " + i);
            ho.setNoPersons(2);
            ho.setAttr(IHotelConsts.VATPROP, "7%");
            iServices.addElem(hotel, ho);
        }
    }

    @Test
    public void test2() {
        resetHotel();
        addList(HOTEL, 100);
        addList(HOTEL1, 90);
        List<HotelServices> hList = iServices.getList(HOTEL);
        assertEquals(100, hList.size());

        hList = iServices.getList(HOTEL1);
        assertEquals(90, hList.size());
        HotelServices ho = hList.get(10);
        ho.setNoPersons(9);
        iServices.changeElem(HOTEL1, ho);
        hList = iServices.getList(HOTEL1);
        assertEquals(90, hList.size());
        ho = hList.get(10);
        assertEquals(9, ho.getNoPersons());

        hList = iServices.getList(HOTEL);
        ho = hList.get(10);
        assertEquals(2, ho.getNoPersons());
    }

    @Test
    public void test3() {
        resetHotel();
        addList(HOTEL, 100);
        addList(HOTEL1, 90);
        List<HotelServices> hList = iServices.getList(HOTEL1);
        HotelServices ho = hList.get(10);
        String name = ho.getName();
        iServices.deleteElem(HOTEL1, ho);
        hList = iServices.getList(HOTEL1);
        assertEquals(89, hList.size());
        for (HotelServices hos : hList) {
            assertFalse(name.equals(hos.getName()));
        }
        hList = iServices.getList(HOTEL);
        assertEquals(100, hList.size());
    }

    @Test
    public void test4() {
        resetHotel();
        addList(HOTEL, 100);
        addList(HOTEL1, 90);
        iServices.deleteAll(HOTEL);
        List<HotelServices> hList = iServices.getList(HOTEL);
        assertTrue(hList.isEmpty());
        hList = iServices.getList(HOTEL1);
        assertEquals(90, hList.size());
    }
}
