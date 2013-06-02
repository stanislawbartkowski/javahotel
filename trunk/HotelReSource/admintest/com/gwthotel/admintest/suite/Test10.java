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

import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.shared.IHotelConsts;

public class Test10 extends TestHelper {

    @Before
    public void before() {
        createHotels();
        iCustomers.deleteAll(HOTEL);
        iCustomers.deleteAll(HOTEL1);
    }

    @Test
    public void test1() {
        List<HotelCustomer> hList = iCustomers.getList(HOTEL);
        assertTrue(hList.isEmpty());
        HotelCustomer co = new HotelCustomer();
        co.setName("C001");
        co.setAttr(IHotelConsts.CUSTOMEREMAILPROP, "aaaaa");
        iCustomers.addElem(HOTEL, co);
        hList = iCustomers.getList(HOTEL);
        assertEquals(1, hList.size());
        co = hList.get(0);
        assertEquals("aaaaa", co.getAttr(IHotelConsts.CUSTOMEREMAILPROP));
    }

    @Test
    public void test2() {
        HotelCustomer co = new HotelCustomer();
        co.setName("C001");
        co.setAttr(IHotelConsts.CUSTOMEREMAILPROP, "aaaaa");
        iCustomers.addElem(HOTEL, co);

        co = new HotelCustomer();
        co.setName("C001");
        co.setAttr(IHotelConsts.CUSTOMEREMAILPROP, "ccccc");
        iCustomers.changeElem(HOTEL, co);
        List<HotelCustomer> hList = iCustomers.getList(HOTEL);
        co = hList.get(0);
        assertEquals("ccccc", co.getAttr(IHotelConsts.CUSTOMEREMAILPROP));

        co = new HotelCustomer();
        co.setName("C002");
        co.setAttr(IHotelConsts.CUSTOMEREMAILPROP, "fffff");
        co.setAttr(IHotelConsts.CUSTOMERFIRSTNAMEPROP, "Hi");
        iCustomers.addElem(HOTEL, co);
        hList = iCustomers.getList(HOTEL);
        assertEquals(2, hList.size());
        co = new HotelCustomer();
        co.setName("C001");
        iCustomers.deleteElem(HOTEL, co);
        hList = iCustomers.getList(HOTEL);
        assertEquals(1, hList.size());
        co = hList.get(0);
        assertEquals("C002", co.getName());
        assertEquals("Hi", co.getAttr(IHotelConsts.CUSTOMERFIRSTNAMEPROP));
    }

}
