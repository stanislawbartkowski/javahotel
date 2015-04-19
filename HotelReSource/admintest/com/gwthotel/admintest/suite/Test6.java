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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.ISharedConsts;

public class Test6 extends TestHelper {

    @Before
    public void resetHotel() {
        clearObjects();
        createHotels();
    }

    @Test
    public void test1() {
        HotelServices ho = new HotelServices();
        ho.setName("1p1");
        ho.setDescription("One person in one person room");
        ho.setNoPersons(2);
        ho.setNoChildren(3);
        ho.setNoExtraBeds(4);
        ho.setPerperson(false);
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        ho = iServices.addElem(getH(HOTEL), ho);
        System.out.println(ho.getCreationPerson());
        assertEquals("user", ho.getCreationPerson());
        assertEquals("user", ho.getModifPerson());
        assertNotNull(ho.getCreationDate());
        assertNotNull(ho.getModifDate());
        assertEquals(2, ho.getNoPersons());
        assertEquals(3, ho.getNoChildren());
        assertEquals(4, ho.getNoExtraBeds());
        assertFalse(ho.isPerperson());

        List<HotelServices> hList = iServices.getList(getH(HOTEL));
        assertEquals(1, hList.size());
        ho = hList.get(0);
        assertEquals("1p1", ho.getName());
        assertEquals(HOTEL, ho.getAttr(ISharedConsts.OBJECTPROP));
        assertEquals("One person in one person room", ho.getDescription());
        assertEquals(2, ho.getNoPersons());
        assertEquals("7%", ho.getAttr(IHotelConsts.VATPROP));

        hList = iServices.getList(getH(HOTEL1));
        assertEquals(0, hList.size());
    }

    private void addList(String hotel, int no) {
        for (int i = 0; i < no; i++) {
            HotelServices ho = new HotelServices();
            ho.setName("" + i);
            ho.setDescription(hotel + "Desc " + i);
            ho.setNoPersons(2);
            ho.setAttr(IHotelConsts.VATPROP, "7%");
            iServices.addElem(getH(hotel), ho);
        }
    }

    @Test
    public void test2() {
        addList(HOTEL, 100);
        addList(HOTEL1, 90);
        List<HotelServices> hList = iServices.getList(getH(HOTEL));
        assertEquals(100, hList.size());

        hList = iServices.getList(getH(HOTEL1));
        assertEquals(90, hList.size());
        HotelServices ho = hList.get(10);
        ho.setNoPersons(9);
        ho.setPerperson(true);
        String sym = ho.getName();
        iServices.changeElem(getH(HOTEL1), ho);
        hList = iServices.getList(getH(HOTEL1));
        assertEquals(90, hList.size());
        // ho = hList.get(10);
        ho = iServices.findElem(getH(HOTEL1), sym);
        assertEquals(9, ho.getNoPersons());
        assertTrue(ho.isPerperson());

        hList = iServices.getList(getH(HOTEL));
        ho = hList.get(10);
        assertEquals(2, ho.getNoPersons());
    }

    @Test
    public void test3() {
        addList(HOTEL, 100);
        addList(HOTEL1, 90);
        List<HotelServices> hList = iServices.getList(getH(HOTEL1));
        HotelServices ho = hList.get(10);
        String name = ho.getName();
        iServices.deleteElem(getH(HOTEL1), ho);
        hList = iServices.getList(getH(HOTEL1));
        assertEquals(89, hList.size());
        for (HotelServices hos : hList) {
            assertFalse(name.equals(hos.getName()));
        }
        hList = iServices.getList(getH(HOTEL));
        assertEquals(100, hList.size());
    }

    @Test
    public void test4() {
        addList(HOTEL, 100);
        addList(HOTEL1, 90);
        List<HotelServices> hList = iServices.getList(getH(HOTEL));
        iServices.deleteElem(getH(HOTEL), hList.get(0));
        hList = iServices.getList(getH(HOTEL));
        assertEquals(99, hList.size());
        hList = iServices.getList(getH(HOTEL1));
        assertEquals(90, hList.size());
    }
}
