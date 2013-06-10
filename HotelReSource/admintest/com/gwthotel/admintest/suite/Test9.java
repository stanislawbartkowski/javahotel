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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;

public class Test9 extends TestHelper {

    private static final String SERVICE1 = "1p1";
    private static final String SERVICE2 = "1p2";

    @Before
    public void before() {
        createHotels();
        iPrice.deleteAll(getH(HOTEL));
        iPrice.deleteAll(getH(HOTEL1));
        iServices.deleteAll(getH(HOTEL));
        iServices.deleteAll(getH(HOTEL1));
        iPriceElem.deleteAll(getH(HOTEL));
        iPriceElem.deleteAll(getH(HOTEL1));
        iRooms.deleteAll(getH(HOTEL));
        iRooms.deleteAll(getH(HOTEL1));
    }

    @Test
    public void test1() {
        List<HotelRoom> hList = iRooms.getList(getH(HOTEL));
        assertTrue(hList.isEmpty());
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        hList = iRooms.getList(getH(HOTEL));
        assertEquals(1, hList.size());
        ho = hList.get(0);
        assertEquals(3, ho.getNoPersons());
        ho.setNoPersons(1);
        iRooms.changeElem(getH(HOTEL), ho);
        hList = iRooms.getList(getH(HOTEL));
        assertEquals(1, hList.size());
        ho = hList.get(0);
        assertEquals(1, ho.getNoPersons());
    }

    @Test
    public void test2() {
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        ho = new HotelRoom();
        ho.setName("P11");
        ho.setNoPersons(1);
        iRooms.addElem(getH(HOTEL), ho);
        List<HotelRoom> hList = iRooms.getList(getH(HOTEL));
        assertEquals(2, hList.size());
        ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.deleteElem(getH(HOTEL), ho);
        hList = iRooms.getList(getH(HOTEL));
        assertEquals(1, hList.size());
        ho = hList.get(0);
        assertEquals("P11", ho.getName());
    }

    @Test
    public void test3() {
        for (int no = 0; no < 100; no++) {
            HotelRoom ho = new HotelRoom();
            ho.setName("P" + no);
            ho.setNoPersons(no);
            iRooms.addElem(getH(HOTEL), ho);
            ho = new HotelRoom();
            ho.setName("R" + no);
            ho.setNoPersons(no);
            iRooms.addElem(getH(HOTEL1), ho);
        }
        List<HotelRoom> hList = iRooms.getList(getH(HOTEL));
        assertEquals(100, hList.size());
        hList = iRooms.getList(getH(HOTEL1));
        assertEquals(100, hList.size());
        iRooms.deleteAll(getH(HOTEL1));
        hList = iRooms.getList(getH(HOTEL));
        assertEquals(100, hList.size());
        hList = iRooms.getList(getH(HOTEL1));
        assertTrue(hList.isEmpty());
    }

    private void addServices() {
        HotelServices ho = new HotelServices();
        ho.setName(SERVICE1);
        ho.setDescription("One person in one person room");
        ho.setNoPersons(2);
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        iServices.addElem(getH(HOTEL), ho);

        ho = new HotelServices();
        ho.setName(SERVICE2);
        ho.setDescription("One person in one person room");
        ho.setNoPersons(2);
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        iServices.addElem(getH(HOTEL), ho);
    }

    @Test
    public void test4() {
        addServices();
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        List<HotelServices> sList = iRooms.getRoomServices(getH(HOTEL), "P10");
        assertTrue(sList.isEmpty());
        List<String> stList = new ArrayList<String>();
        stList.add(SERVICE1);
        iRooms.setRoomServices(getH(HOTEL), "P10", stList);
        sList = iRooms.getRoomServices(getH(HOTEL), "P10");
        assertEquals(1, sList.size());

        stList = new ArrayList<String>();
        stList.add(SERVICE2);
        iRooms.setRoomServices(getH(HOTEL), "P10", stList);
        sList = iRooms.getRoomServices(getH(HOTEL), "P10");
        assertEquals(1, sList.size());
        HotelServices se = sList.get(0);
        assertEquals(SERVICE2, se.getName());
    }

    @Test
    public void test5() {
        addServices();
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        List<String> stList = new ArrayList<String>();
        stList.add(SERVICE1);
        stList.add(SERVICE2);
        iRooms.setRoomServices(getH(HOTEL), "P10", stList);
        List<HotelServices> sList = iRooms.getRoomServices(getH(HOTEL), "P10");
        assertEquals(2, sList.size());

        HotelServices serv = new HotelServices();
        serv.setName(SERVICE1);
        iServices.deleteElem(getH(HOTEL), serv);
        sList = iRooms.getRoomServices(getH(HOTEL), "P10");
        assertEquals(1, sList.size());
    }

    @Test
    public void test6() {
        List<String> sList = new ArrayList<String>();
        for (int s = 0; s < 10; s++) {
            HotelServices ho = new HotelServices();
            ho.setName("S" + s);
            ho.setDescription("Super service " + s);
            ho.setNoPersons(s);
            ho.setAttr(IHotelConsts.VATPROP, "7%");
            iServices.addElem(getH(HOTEL), ho);
            sList.add("S" + s);
        }
        for (int no = 0; no < 100; no++) {
            HotelRoom ho = new HotelRoom();
            ho.setName("P" + no);
            ho.setNoPersons(no);
            iRooms.addElem(getH(HOTEL), ho);
            iRooms.setRoomServices(getH(HOTEL), "P" + no, sList);
        }
        for (int no = 0; no < 100; no++) {
            List<HotelServices> seList = iRooms.getRoomServices(getH(HOTEL),
                    "P" + no);
            assertEquals(10, seList.size());
        }
        for (int s = 0; s < 5; s++) {
            HotelServices ho = new HotelServices();
            ho.setName("S" + s);
            iServices.deleteElem(getH(HOTEL), ho);
        }
        for (int no = 0; no < 100; no++) {
            List<HotelServices> seList = iRooms.getRoomServices(getH(HOTEL),
                    "P" + no);
            assertEquals(5, seList.size());
        }
    }
}
