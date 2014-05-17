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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;

public class Test30 extends TestHelper {

    @Test
    public void test1() {

        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        String name = p.getName();
        Long id = p.getId();
        assertNotNull(id);
        p = iCustomers.findElemById(getH(HOTEL), id);
        assertNotNull(p);
        assertEquals(name, p.getName());
    }

    @Test
    public void test2() {
        HotelServices ho = new HotelServices();
        ho.setName("1p1");
        ho.setDescription("One person in one person room");
        ho.setNoPersons(2);
        ho.setNoChildren(3);
        ho.setNoExtraBeds(4);
        ho.setPerperson(false);
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        ho = iServices.addElem(getH(HOTEL), ho);
        Long id = ho.getId();
        assertNotNull(id);
        ho = iServices.findElemById(getH(HOTEL), id);
        assertNotNull(ho);
        assertEquals("1p1", ho.getName());
    }

}
