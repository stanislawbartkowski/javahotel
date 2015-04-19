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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.gwthotel.hotel.rooms.HotelRoom;

public class Test27 extends TestHelper {

    @Before
    public void before() {
        clearObjects();
        createHotels();
    }

    @Test
    public void test1() {
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(1);
        ho.setNoChildren(2);
        ho.setNoExtraBeds(3);
        iRooms.addElem(getH(HOTEL), ho);
        ho = iRooms.findElem(getH(HOTEL), "P10");
        assertNotNull(ho);
        assertEquals(1, ho.getNoPersons());
        assertEquals(3, ho.getNoExtraBeds());
        assertEquals(2, ho.getNoChildren());
    }

}
