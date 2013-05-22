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
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.Person;
import com.gwthotel.admin.Role;
import com.gwthotel.admin.VatTax;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.DecimalUtils;

/**
 * @author hotel
 * 
 */
public class Test1 extends TestHelper {

    @Test
    public void test1() {
        iAdmin.clearAll();
        List<Hotel> aList = iAdmin.getListOfHotels();
        assertEquals(0, aList.size());
        Hotel ho = new Hotel();
        ho.setName("hotel");
        ho.setDescription("Super hotel");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifHotel(ho, roles);
        aList = iAdmin.getListOfHotels();
        assertEquals(1, aList.size());
        ho = aList.get(0);
        assertEquals("hotel", ho.getName());
        assertEquals("Super hotel", ho.getDescription());
    }

    @Test
    public void test2() {
        iAdmin.clearAll();
        List<Person> aList = iAdmin.getListOfPersons();
        assertEquals(0, aList.size());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifPerson(pe, roles);
        aList = iAdmin.getListOfPersons();
        assertEquals(1, aList.size());
        pe = aList.get(0);
        assertEquals("user", pe.getName());
        assertEquals("user name", pe.getDescription());
    }

    @Test
    public void test3() {
        assertEquals(3, iRoles.getList().size());
        for (Role r : iRoles.getList()) {
            assertNotNull(r.getName());
            assertNotNull(r.getDescription());
        }
    }
    
    @Test
    public void test4() {
        assertEquals(5, iTaxes.getList().size());
        int ok = 0;
        for (VatTax r : iTaxes.getList()) {
            assertNotNull(r.getName());
            assertNotNull(r.getDescription());
            String level = r.getAttr(IHotelConsts.VATLEVELPROP);
            System.out.println(level);
            BigDecimal b = r.getAttrBig(IHotelConsts.VATLEVELPROP);
            if (b != null) {
                System.out.println(b);
                if (b.equals(DecimalUtils.toBig("0"))) ok++;
                if (b.equals(DecimalUtils.toBig("7"))) ok++;
                if (b.equals(DecimalUtils.toBig("20"))) ok++;
                if (b.equals(DecimalUtils.toBig("22"))) ok++;
            }
        }
        System.out.println("ok=" + ok);
        assertEquals(4,ok);
    }
}
