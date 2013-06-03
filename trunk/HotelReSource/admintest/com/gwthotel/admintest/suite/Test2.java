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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.Person;

import static org.junit.Assert.*;

public class Test2 extends TestHelper {

    @Test
    public void test1() {
        iAdmin.clearAll();
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifPerson(pe, roles);
        Hotel ho = new Hotel();
        ho.setName("hotel");
        ho.setDescription("Super hotel");
        roles = new ArrayList<HotelRoles>();
        HotelRoles rol = new HotelRoles(pe);
        rol.getRoles().add("admin");
        roles.add(rol);
        iAdmin.addOrModifHotel(ho, roles);
        // now check for hotel
        List<HotelRoles> hList = iAdmin.getListOfRolesForHotel("hotel");
        assertEquals(1, hList.size());
        for (HotelRoles hot : hList) {
            assertEquals("user", hot.getObject().getName());
            assertEquals(1, hot.getRoles().size());
            assertEquals("admin", hot.getRoles().get(0));
        }
        // now check for person
        hList = iAdmin.getListOfRolesForPerson("user");
        assertEquals(1, hList.size());
        for (HotelRoles hot : hList) {
            assertEquals("hotel", hot.getObject().getName());
            assertEquals(1, hot.getRoles().size());
            assertEquals("admin", hot.getRoles().get(0));
        }
    }

    @Test
    public void test2() {
        test1();
        Hotel ho = new Hotel();
        ho.setName("hotel");
        ho.setDescription("Super hotel");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifHotel(ho, roles);
        // now check person, should be empty
        List<HotelRoles> hList = iAdmin.getListOfRolesForPerson("user");
        assertEquals(0, hList.size());
    }

    @Test
    public void test3() {
        iAdmin.clearAll();
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        for (int i = 0; i < 100; i++) {
            Hotel ho = new Hotel();
            ho.setName("hotel" + i);
            ho.setDescription("Avoid this hotel " + i);
            HotelRoles rol = new HotelRoles(ho);
            rol.getRoles().add("admin");
            rol.getRoles().add("man");
            roles.add(rol);
            iAdmin.addOrModifHotel(ho, new ArrayList<HotelRoles>());
        }
        iAdmin.addOrModifPerson(pe, roles);
        List<HotelRoles> hList = iAdmin.getListOfRolesForPerson("user");
        assertEquals(100,hList.size());
    }
    
    @Test
    public void test4() {
        test3();
        List<Hotel> h = iAdmin.getListOfHotels();
        assertEquals(100,h.size());
        iAdmin.removeHotel("hotel10");
        List<HotelRoles> hList = iAdmin.getListOfRolesForPerson("user");
        assertEquals(99,hList.size());  
        h = iAdmin.getListOfHotels();
        assertEquals(99,h.size());
        boolean exists = false;
        for (Hotel ho : h) {
            if (ho.getName().equals("hotel10")) {
                exists = true;
            }
        }
        assertFalse(exists);
    }
    
    @Test
    public void test5() {
        test3();
        iAdmin.removePerson("user");
        List<HotelRoles> hList = iAdmin.getListOfRolesForPerson("user");
        assertNull(hList);
        List<Hotel> h = iAdmin.getListOfHotels();
        assertEquals(100,h.size());
        List<Person> pList = iAdmin.getListOfPersons();
        assertEquals(0,pList.size());
        
    }

}
